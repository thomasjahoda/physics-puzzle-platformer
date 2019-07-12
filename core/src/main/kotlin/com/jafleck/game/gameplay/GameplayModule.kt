package com.jafleck.game.gameplay

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.game.assets.GdxHoloSkin
import com.jafleck.game.config.PhysicsConfiguration
import com.jafleck.game.gadgets.BallThrowerGadget
import com.jafleck.game.gameplay.standaloneentitylisteners.SyncRemovedBodiesToWorldEntityListener
import com.jafleck.game.gameplay.systems.*
import com.jafleck.game.gameplay.systems.debug.CursorDebugSystem
import com.jafleck.game.gameplay.systems.debug.PlayerManualTeleportDebugSystem
import com.jafleck.game.gameplay.systems.input.CurrentCursorPositionInputSystem
import com.jafleck.game.gameplay.systems.input.PlayerGadgetActivationSystem
import com.jafleck.game.gameplay.systems.input.PlayerMovementInputSystem
import com.jafleck.game.gameplay.systems.physicssync.SyncMovingBodySystem
import com.jafleck.game.gameplay.systems.physicssync.SyncRotatingBodySystem
import com.jafleck.game.gameplay.systems.visual.RenderDrawableRectangleComponentsSystem
import com.jafleck.game.gameplay.systems.visual.ShapeRenderSystem
import com.jafleck.game.gameplay.ui.*
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.input.UiInputMultiplexer
import com.jafleck.game.util.listeners.EntityFamilyListener
import ktx.box2d.createWorld
import ktx.box2d.earthGravity
import org.koin.core.module.Module
import org.koin.dsl.module

interface EngineLogicLoader {
    fun load(engine: Engine)
}

val gameplayModule: Module = module {
    // ui
    single { GameCamera() }
    single { GameViewport(10f, 10f, get()) }
    single { UiCamera() }
    single { UiViewport(get()) }
    single { Stage(get<UiViewport>()) }
    single { SpriteBatch() }
    single { UiInputMultiplexer() }
    single { GameInputMultiplexer() }
    single { GdxHoloSkin(get()) }
    single { PlayScreen(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), getOrNull(), getOrNull()) }
    // gadgets
    single { BallThrowerGadget(get()) }
    // entity component system
    single { Engine() }
    single { CurrentCursorPositionInputSystem(get(), get()) }
    if (PhysicsConfiguration.showCursorWorldPosition) {
        single { CursorDebugSystem(get(), get(), get()) }
        single { PlayerManualTeleportDebugSystem(get(), get()) }
    }
    single {
        val systems = mutableListOf<EntitySystem>()
        systems.add(get<CurrentCursorPositionInputSystem>())
        systems.addAll(listOf(
            // input handling
            PlayerMovementInputSystem(get(), get()),
            PlayerGadgetActivationSystem(get(), get()),

            // physics
            WaterSystem(get()),
            PhysicsSimulationStepSystem(get()),
            SyncMovingBodySystem(),
            SyncRotatingBodySystem(),

            //  logic
            RemoveEntityAfterDurationSystem(),
            PlayerMovementSystem(),

            // rendering
            TrackPlayerWithCameraSystem(get()),
            ShapeRenderSystem(get()),
            RenderDrawableRectangleComponentsSystem(get(), get(), get())
        ))

        withItIfNotNull(this@single.getOrNull<CursorDebugSystem>()) {
            systems.add(it)
        }
        withItIfNotNull(this@single.getOrNull<PlayerManualTeleportDebugSystem>()) {
            systems.add(it)
        }

        systems.forEachIndexed { index, entitySystem -> entitySystem.priority = index }

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
    // physics
    single {
        Box2D.init()
        createWorld(gravity = earthGravity)
    }
    if (PhysicsConfiguration.debugRendering) {
        single { Box2DDebugRenderer() }
    }
}
