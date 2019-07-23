package com.jafleck.game.gameplay.controlandmainphases

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.game.gameplay.ui.GameViewport
import com.jafleck.game.util.logger

/**
 * Executes the main game logic phases.
 */
class GameLogicTickExecutor(
    private val engine: Engine,
    private val postSystemUpdatePhaseActionExecutor: PostSystemUpdatePhaseActionExecutor,
    private val world: World,
    private val gameViewport: GameViewport,
    private val box2DDebugRenderer: Box2DDebugRenderer?
) {

    private val logger = logger(this::class)

    fun updateAndRenderTick(deltaSeconds: Float) {
        gameViewport.apply()
        engine.update(deltaSeconds)
        postSystemUpdatePhaseActionExecutor.update()
        box2DDebugRenderer?.render(world, gameViewport.camera.combined)
    }
}
