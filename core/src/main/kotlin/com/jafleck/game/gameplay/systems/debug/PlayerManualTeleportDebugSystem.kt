package com.jafleck.game.gameplay.systems.debug

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.gameplay.systems.PlayerEntitySystem
import com.jafleck.game.gameplay.systems.input.CurrentCursorPositionInputSystem
import com.jafleck.game.gameplay.ui.GameViewport
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.logger


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

