package com.jafleck.game.gameplay

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.game.config.GeneralDebugConfiguration
import com.jafleck.game.config.PhysicsConfiguration
import com.jafleck.game.gadgets.BallThrowerGadget
import com.jafleck.game.gadgets.RopeThrowerGadget
import com.jafleck.game.gameplay.standaloneentitylisteners.SyncRemovedBodiesToWorldEntityListener
import com.jafleck.game.gameplay.standaloneentitylisteners.TriangulateVisualPolygonShapesEntityListener
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
import com.jafleck.game.util.box2d.ContactListenerMultiplexer
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.input.UiInputMultiplexer
import com.jafleck.game.util.listeners.EntityFamilyListener
import ktx.box2d.createWorld
import ktx.box2d.earthGravity
import org.koin.dsl.module

interface EngineLogicLoader {
    fun load(engine: Engine)
}

internal val gameplayUiModule = module {
    single { GameCamera() }
    single { GameViewport(25f, 15f, get()) }
    single { UiCamera() }
    single { UiViewport(get()) }
    single { Stage(get<UiViewport>()) }
    single { SpriteBatch() }
    single { UiInputMultiplexer() }
    single { GameInputMultiplexer() }
    single {
        PlayScreen(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
            getOrNull(), getOrNull(), getOrNull(), getOrNull())
    }
}

internal val gameplayGadgetsModule = module {
    single { BallThrowerGadget(get()) }
    single { RopeThrowerGadget(get(), get()) }
}

internal val gameplayEntityComponentSystemBasicsModule = module {
    single { Engine() }
}

internal val gameplayEntityComponentSystemLogicModule = module {
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
            WaterSystem(get(), get()),
            PhysicsSimulationStepSystem(get()),
            SyncMovingBodySystem(),
            SyncRotatingBodySystem(),

            //  logic
            RemoveEntityAfterDurationSystem(),
            PlayerMovementSystem(),
            ThrownRopeSystem(get(), get()),

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
            SyncRemovedBodiesToWorldEntityListener(get()),
            TriangulateVisualPolygonShapesEntityListener()
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
}

internal val gameplayPhysicsModule = module {
    single { ContactListenerMultiplexer() }
    single {
        Box2D.init()
        val world = createWorld(gravity = earthGravity)
        world.setContactListener(get<ContactListenerMultiplexer>())
        world
    }
    if (PhysicsConfiguration.debugRendering) {
        single { Box2DDebugRenderer() }
    }
    // general debug tooling
    if (GeneralDebugConfiguration.manualTimeControlEnabled) {
        single { ManualTimeControl(get()) }
    }
    if (GeneralDebugConfiguration.showFps) {
        single { FpsCounter() }
    }
}

val gameplayModules = listOf(
    gameplayUiModule,
    gameplayGadgetsModule,
    gameplayEntityComponentSystemBasicsModule,
    gameplayEntityComponentSystemLogicModule,
    gameplayPhysicsModule
)
