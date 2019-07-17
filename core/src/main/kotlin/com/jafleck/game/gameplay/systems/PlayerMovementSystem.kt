package com.jafleck.game.gameplay.systems

import com.badlogic.gdx.math.Vector2
import com.jafleck.game.components.entities.PlayerMovementState
import com.jafleck.game.entities.PlayerEntity


class PlayerMovementSystem : PlayerEntitySystem() {

    private val baseAcceleration = 0.25f

    override fun processPlayer(playerEntity: PlayerEntity, deltaSeconds: Float) {
        if (deltaSeconds == 0f) return

        val effectiveAccelerationForTick = baseAcceleration * (deltaSeconds / (1/60f))
        when (playerEntity.player.movementState) {
            PlayerMovementState.LEFT -> acceleratePlayer(playerEntity, -effectiveAccelerationForTick)
            PlayerMovementState.RIGHT -> acceleratePlayer(playerEntity, effectiveAccelerationForTick)
            PlayerMovementState.NONE -> {
                // breaking automatically
            }
        }
    }

    private fun acceleratePlayer(playerEntity: PlayerEntity, acceleration: Float) {
        playerEntity.body.value.applyLinearImpulse(Vector2(acceleration * playerEntity.body.value.mass, 0f), playerEntity.position.vector, true)
    }
}
