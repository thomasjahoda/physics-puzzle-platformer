package com.jafleck.game.gameplay

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Logger
import com.jafleck.extensions.kotlin.round
import com.jafleck.game.assets.Assets
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.config.LoggingConfig
import com.jafleck.game.config.PhysicsConfiguration
import com.jafleck.game.entities.PlatformEntityCreator
import com.jafleck.game.entities.PlayerEntityCreator
import com.jafleck.game.gameplay.systems.PhysicsSimulationStepSystem
import com.jafleck.game.gameplay.systems.RenderDrawableRectangleComponentsSystem
import com.jafleck.game.gameplay.systems.SyncMovingBodySystem
import com.jafleck.game.gameplay.systems.TrackPlayerWithCameraSystem
import com.jafleck.game.gameplay.ui.PlayScreen
import com.jafleck.game.util.*
import com.jafleck.game.util.ui.GdxHoloSkin
import ktx.box2d.createWorld
import ktx.box2d.earthGravity
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.system.measureNanoTime

private val gameplayModuleLogger = Logger("GameplayModule", LoggingConfig.gameLoggingLevel.asGdxLoggingLevel())

fun createGameplayModule(): Module {
    return module {
        single { GameCamera() }
        single { GameViewport(10f, 10f, get()) }
        single { UiCamera() }
        single { UiViewport(get()) }
        single { Stage(get(UiViewport::class, null, null)) }
        single { ScreenToWorldScalingPropagator() }
        single { SpriteBatch() }
        single { PlayerEntityCreator(get(), get(), get()) }
        single { PlatformEntityCreator(get(), get(), get(), get()) }
        single {
            @Suppress("UNUSED_CHANGED_VALUE")
            Engine().apply {
                var systemPriority = 0
                addSystem(PhysicsSimulationStepSystem(systemPriority++, get()))
                addSystem(SyncMovingBodySystem(systemPriority++))
                addSystem(TrackPlayerWithCameraSystem(systemPriority++, get()))
                addSystem(RenderDrawableRectangleComponentsSystem(systemPriority++, get(), get(), get()))
            }
        }
        single {
            AssetManager().apply {
                Assets.queueAssetsToLoad(this)
                var loadedAssets: Boolean = false
                val nanosTakenToLoadAssets = measureNanoTime { loadedAssets = update(10 * 1000) }
                require(loadedAssets) { "Could not load assets within 10 seconds." }
                val millisTakenToLoadAssets = (nanosTakenToLoadAssets / 1000000f).round(3)
                gameplayModuleLogger.info("Took $millisTakenToLoadAssets ms to load assets")
            }
        }
        single { GdxHoloSkin(get()) }
        single { PlayScreen(get(), get(), get(), get(), get(), get(), get(), get(), getOrNull()) }
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
