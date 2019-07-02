package com.jafleck.game.gameplay.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.jafleck.extensions.libgdxktx.clearScreen
import ktx.app.KtxScreen


class PlayScreen(
    private val stage: Stage,
    private val engine: Engine,
    private val world: World,
    private val camera: OrthographicCamera,
    private val box2DDebugRenderer: Box2DDebugRenderer?
) : KtxScreen {

    init {
        stage.apply {
            // add UI here when necessary
        }
    }

    override fun render(delta: Float) {
        updateAndRender(delta)
    }

    private fun updateAndRender(deltaSeconds: Float) {
        clearScreen(Color.WHITE)
        stage.draw()
        engine.update(deltaSeconds)
        box2DDebugRenderer?.render(world, camera.combined)
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, false)
    }

    override fun hide() {
    }
}
