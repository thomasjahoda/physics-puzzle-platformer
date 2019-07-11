package com.jafleck.game.gameplay.systems.debug

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.gameplay.systems.PlayerEntitySystem
import com.jafleck.game.gameplay.systems.input.CurrentCursorPositionInputSystem
import com.jafleck.game.util.input.GameInputMultiplexer


class PlayerManualTeleportDebugSystem(
    private val gameInputMultiplexer: GameInputMultiplexer,
    private val currentCursorPositionInputSystem: CurrentCursorPositionInputSystem
) : PlayerEntitySystem() {

    private val inputListener = InputListener()
    private var teleportRequested = false

    override fun additionalAddedToEngine(engine: Engine) {
        gameInputMultiplexer.addProcessor(inputListener)
    }

    override fun removedFromEngine(engine: Engine?) {
        gameInputMultiplexer.removeProcessor(inputListener)
    }

    private inner class InputListener : InputAdapter() {

        override fun keyDown(keycode: Int): Boolean {
            if (keycode == Input.Keys.T) {
                teleportRequested = true
            }
            return false
        }
    }

    override fun processPlayer(playerEntity: PlayerEntity) {
        if (teleportRequested) {
            val targetTeleportPosition = currentCursorPositionInputSystem.currentCursorWorldPosition
            teleportRequested = false
            playerEntity.position.vector.set(targetTeleportPosition)
            val body = playerEntity.body.value
            body.setTransform(targetTeleportPosition, body.angle)
            body.isAwake = true
        }
    }
}

