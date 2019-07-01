package com.jafleck.game.gameplay.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.jafleck.extensions.libgdxktx.clearScreen
import ktx.app.KtxScreen


@Suppress("ConstantConditionIf")
class PlayScreen(
    private val stage: Stage,
    private val engine: Engine
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
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, false)
    }

    override fun hide() {
    }
}
