package com.jafleck.game.gameplay.systems.debug

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.jafleck.extensions.kotlin.round
import com.jafleck.game.gameplay.systems.input.CurrentCursorPositionInputSystem
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.libgdx.box2d.entity
import com.jafleck.game.util.logger
import ktx.box2d.Query
import ktx.box2d.query
import ktx.scene2d.Scene2DSkin


class CursorDebugSystem(
    private val currentCursorPositionInputSystem: CurrentCursorPositionInputSystem,
    private val world: World,
    private val gameInputMultiplexer: GameInputMultiplexer
) : EntitySystem() {

    val worldCoordsOfCursorLabel = Label("initial text", Scene2DSkin.defaultSkin)
    private val hoverListener = InputListener()
    private var dumpHoveredObjectPositionRequested = false

    private val logger = logger(this::class)

    override fun addedToEngine(engine: Engine) {
        gameInputMultiplexer.addProcessor(hoverListener)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameInputMultiplexer.removeProcessor(hoverListener)
    }

    private inner class InputListener : InputAdapter() {

        override fun keyDown(keycode: Int): Boolean {
            if (keycode == Input.Keys.I) {
                dumpHoveredObjectPositionRequested = true
            }
            return false
        }
    }

    override fun update(deltaTime: Float) {
        val currentCursorScreenPosition = currentCursorPositionInputSystem.currentCursorScreenPosition
        val currentCursorWorldPosition = currentCursorPositionInputSystem.currentCursorWorldPosition

        worldCoordsOfCursorLabel.setText("${currentCursorWorldPosition.x.round(3).toString().padEnd(8)} // ${currentCursorWorldPosition.y.round(3).toString().padEnd(8)}" +
            "   <>   screen: ${currentCursorScreenPosition.x.toString().padEnd(5)} // ${currentCursorScreenPosition.y.toString().padEnd(5)}")

        if (dumpHoveredObjectPositionRequested) {
            dumpHoveredObjectPositionRequested = false

            dumpDebugInfoForEntityAt(currentCursorWorldPosition)
        }
    }

    private fun dumpDebugInfoForEntityAt(position: Vector2) {
        var foundEntity = false
        val delta = 0.01f
        world.query(position.x - delta, position.y - delta, position.x + delta, position.y + delta) {
            if (!it.testPoint(position)) {
                return@query Query.CONTINUE
            }
            foundEntity = true
            logger.info {
                val entity = it.body.entity
                val componentsOutput = entity.components.joinToString(separator = "\n") {
                    it.toString()
                }
                "Selected entity: $entity  (at position $position)\n" +
                    "============================\n" +
                    "$componentsOutput\n" +
                    "============================"
            }
            return@query Query.STOP
        }
        if (!foundEntity) {
            logger.info { "Could not find entity at $position" }
        }
    }
}

