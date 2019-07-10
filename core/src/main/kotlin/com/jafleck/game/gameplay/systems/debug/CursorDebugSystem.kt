package com.jafleck.game.gameplay.systems.debug

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.jafleck.extensions.kotlin.round
import com.jafleck.game.gameplay.ui.GameViewport
import ktx.scene2d.Scene2DSkin


class CursorDebugSystem(
    private val stage: Stage,
    private val gameViewport: GameViewport
) : EntitySystem() {

    val worldCoordsOfCursorLabel = Label("initial text", Scene2DSkin.defaultSkin)
    private val hoverListener = HoverListener()
    private val currentCursorScreenPosition = Vector2()
    private val currentCursorWorldPosition = Vector2()

    override fun addedToEngine(engine: Engine) {
        stage.addListener(hoverListener)
    }

    override fun removedFromEngine(engine: Engine?) {
        stage.removeListener(hoverListener)
    }

    private inner class HoverListener: InputListener() {

        override fun mouseMoved(event: InputEvent?, x: Float, y: Float): Boolean {
            currentCursorScreenPosition.set(x, y)
            return false
        }
    }

    override fun update(deltaTime: Float) {
        gameViewport.unproject(currentCursorWorldPosition.set(currentCursorScreenPosition))
        worldCoordsOfCursorLabel.setText("${currentCursorWorldPosition.x.round(3)} / ${currentCursorWorldPosition.y.round(3)}")
    }
}

