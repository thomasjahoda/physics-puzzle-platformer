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
import com.jafleck.game.gameplay.controlandmainphases.FinishedMapSuccessfullyHandler
import com.jafleck.game.gameplay.controlandmainphases.GameLogicTickExecutor
import com.jafleck.game.gameplay.controlandmainphases.PostSystemUpdatePhaseActionExecutor
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
import com.jafleck.game.util.ashley.EntityFamilyListener
import com.jafleck.game.util.box2d.ContactListenerMultiplexer
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.input.UiInputMultiplexer
import ktx.box2d.createWorld
import ktx.box2d.earthGravity
import org.koin.dsl.module

internal val entityComponentSystemBasicsModule = module {
    single { Engine() }
}

internal val utilitiesModule = module {
    single { GameViewport(25f, 15f, get()) }
    single { GameCamera() }
    single { SpriteBatch() }
    single { GameInputMultiplexer() }
}

interface EngineLogicLoader {
    fun load(engine: Engine)
}

internal val entityComponentSystemLogicModule = module {
    single { CurrentCursorPositionInputSystem(get(), get()) }
    if (PhysicsConfiguration.showCursorWorldPosition) {
        single { CursorDebugSystem(get(), get(), get()) }
        single { PlayerManualTeleportDebugSystem(get(), get()) }
    }
    single {
        val systems = mutableListOf<EntitySystem>()
        systems.add(get<CurrentCursorPositionInputSystem>())
        systems.addAll(listOf(
            // == input handling
            PlayerMovementInputSystem(get(), get()),
            PlayerGadgetActivationSystem(get(), get()),

            // == physics
            // basic
            PhysicsSimulationStepSystem(get()),
            // generic
            SyncMovingBodySystem(),
            SyncRotatingBodySystem(),
            EntityCollisionTrackingZoneSystem(get()),
            // entities
            WaterSystem(get()),
            DeathZoneSystem(),
            GoalZoneSystem(get()),

            // == logic
            RemoveEntityAfterDurationSystem(),
            PlayerMovementSystem(),
            ThrownRopeSystem(get(), get()),

            // == rendering
            TrackPlayerWithCameraSystem(get()),
            ShapeRenderSystem(get()),
            RenderDrawableRectangleComponentsSystem(get(), get(), get())

            // == phase-hook: at end of tick
//            get<PostSystemUpdatePhaseActionExecutor>()
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

internal val controlAndMainPhasesModule = module {
    single { GameLogicTickExecutor(get(), get(), get(), get(), getOrNull()) }
    single { PostSystemUpdatePhaseActionExecutor() }
    single { FinishedMapSuccessfullyHandler(get(), get(), get()) }
}

internal val gadgetsModule = module {
    single { BallThrowerGadget(get()) }
    single { RopeThrowerGadget(get(), get()) }
}

internal val physicsModule = module {
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

internal val uiModule = module {
    single { UiCamera() }
    single { UiViewport(get()) }
    single { Stage(get<UiViewport>()) }
    single { UiInputMultiplexer() }
    single { TransparentColorBackgroundImageFactory(get()) }
    single { MenuRow(get(), get(), get()) }
    single { DebugRow(getOrNull(), getOrNull(), getOrNull()) }
    single {
        PlayScreen(get(), get(), get(), get(), get(), get(), get(), get(), get(),
            getOrNull(), getOrNull())
    }
}

val gameplayModules = listOf(
    utilitiesModule,
    gadgetsModule,
    entityComponentSystemBasicsModule,
    entityComponentSystemLogicModule,
    controlAndMainPhasesModule,
    physicsModule,
    uiModule
)
