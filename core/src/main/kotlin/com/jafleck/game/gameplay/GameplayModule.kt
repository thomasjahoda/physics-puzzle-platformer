package com.jafleck.game.gameplay

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntityListener
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Logger
import com.jafleck.extensions.kotlin.round
import com.jafleck.game.assets.Assets
import com.jafleck.game.assets.GdxHoloSkin
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.config.LoggingConfig
import com.jafleck.game.config.PhysicsConfiguration
import com.jafleck.game.entities.PlatformEntityCreator
import com.jafleck.game.entities.PlayerEntityCreator
import com.jafleck.game.entities.ThrownBallEntityCreator
import com.jafleck.game.gadgets.BallThrowerGadget
import com.jafleck.game.gameplay.standaloneentitylisteners.SyncRemovedBodiesToWorldEntityListener
import com.jafleck.game.gameplay.systems.*
import com.jafleck.game.gameplay.ui.PlayScreen
import com.jafleck.game.util.*
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.input.UiInputMultiplexer
import com.jafleck.game.util.listeners.EntityFamilyListener
import ktx.box2d.createWorld
import ktx.box2d.earthGravity
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.system.measureNanoTime

private val gameplayModuleLogger = Logger("GameplayModule", LoggingConfig.gameLoggingLevel.asGdxLoggingLevel())

interface EngineLogicLoader {
    fun load(engine: Engine)
}

fun createGameplayModule(): Module {
    return module {
        // ui
        single { GameCamera() }
        single { GameViewport(10f, 10f, get()) }
        single { UiCamera() }
        single { UiViewport(get()) }
        single { Stage(get(UiViewport::class, null, null)) }
        single { ScreenToWorldScalingPropagator() }
        single { SpriteBatch() }
        single { UiInputMultiplexer() }
        single { GameInputMultiplexer() }
        single { GdxHoloSkin(get()) }
        single { PlayScreen(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), getOrNull()) }
        // gadgets
        single { BallThrowerGadget(get()) }
        // entities
        single { PlayerEntityCreator(get(), get(), get(BallThrowerGadget::class, null, null), get()) }
        single { PlatformEntityCreator(get(), get(), get(), get()) }
        single { ThrownBallEntityCreator(get(), get(), get(), get()) }
        // entity component system
        single { Engine() }
        single {
            var systemPriority = 0
            @Suppress("UNUSED_CHANGED_VALUE")
            val systems = listOf(
                // input handling
                PlayerMovementInputSystem(systemPriority++, get(), get()),
                PlayerGadgetActivationSystem(systemPriority++, get(), get()),

                // physics
                PhysicsSimulationStepSystem(systemPriority++, get()),
                SyncMovingBodySystem(systemPriority++),
                SyncRotatingBodySystem(systemPriority++),

                // logic
                RemoveEntityAfterDurationSystem(systemPriority++),
                PlayerMovementSystem(systemPriority++),

                // rendering
                TrackPlayerWithCameraSystem(systemPriority++, get()),
                RenderDrawableRectangleComponentsSystem(systemPriority++, get(), get(), get())
            )

            val standaloneEntityListeners = listOf<EntityListener>(
                SyncRemovedBodiesToWorldEntityListener(get())
            )

            val logicLoader: EngineLogicLoader = object : EngineLogicLoader {
                override fun load(engine: Engine) {
                    systems.forEach {
                        engine.addSystem(it)
                    }
                    standaloneEntityListeners.forEach {
                        if (it is EntityFamilyListener) {
                            engine.addEntityListener(it.family, it)
                        } else {
                            engine.addEntityListener(it)
                        }
                    }
                }
            }
            logicLoader
        }
        single {
            AssetManager().apply {
                Assets.queueAssetsToLoad(this)
                var loadedAssets = false
                val nanosTakenToLoadAssets = measureNanoTime { loadedAssets = update(10 * 1000) }
                require(loadedAssets) { "Could not load assets within 10 seconds." }
                val millisTakenToLoadAssets = (nanosTakenToLoadAssets / 1000_000f).round(3)
                gameplayModuleLogger.info("Took ${millisTakenToLoadAssets}ms to load assets")
            }
        }
        single { MapLoader(get(), get()) }
        single {
            Box2D.init()
            createWorld(gravity = earthGravity)
        }
        if (PhysicsConfiguration.debugRendering) {
            single { Box2DDebugRenderer() }
        }
    }
}
