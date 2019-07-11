package com.jafleck.game.gameplay.systems.input

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.jafleck.extensions.kotlin.round
import com.jafleck.game.gameplay.ui.GameViewport
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.logger
import ktx.box2d.Query
import ktx.scene2d.Scene2DSkin


class CurrentCursorPositionInputSystem(
    private val gameViewport: GameViewport,
    private val gameInputMultiplexer: GameInputMultiplexer
) : EntitySystem() {

    private val inputListener = InputListener()
    val currentCursorScreenPosition = Vector2()
    val currentCursorWorldPosition = Vector2()

    private val logger = logger(this::class)

    override fun addedToEngine(engine: Engine) {
        gameInputMultiplexer.addProcessor(inputListener)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameInputMultiplexer.removeProcessor(inputListener)
    }

    private inner class InputListener : InputAdapter() {

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            updateCursorPosition(screenX, screenY)
            return false
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            updateCursorPosition(screenX, screenY)
            return false
        }

        private fun updateCursorPosition(screenX: Int, screenY: Int) {
            currentCursorScreenPosition.set(screenX.toFloat(), screenY.toFloat())
        }
    }

    override fun update(deltaTime: Float) {
        gameViewport.unproject(currentCursorWorldPosition.set(currentCursorScreenPosition))
    }
}

