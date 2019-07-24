package com.jafleck.game.gameplay.systems

import com.badlogic.gdx.math.Vector2
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.util.logger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class PlayerMovementSystem : PlayerEntitySystem() {

    private val baseAcceleration = 0.25f
    private val maxControlSpeed = 30f

    private val logger = logger(this::class)

    override fun processPlayer(playerEntity: PlayerEntity, deltaSeconds: Float) {
        if (deltaSeconds == 0f) return

        val maxAccelerationForTick = baseAcceleration * (deltaSeconds / (1 / 60f))
        acceleratePlayerLinearlyBasedOnMovementState(playerEntity, maxAccelerationForTick)
    }

    private fun acceleratePlayerLinearlyBasedOnMovementState(playerEntity: PlayerEntity, maxAccelerationForTick: Float) {
        val accelerationDirectionSign = playerEntity.player.movementState.horizontalDirectionSign
        val differenceToMaxSpeed = abs((maxControlSpeed * accelerationDirectionSign) - playerEntity.body.value.linearVelocity.x)
        val effectiveAccelerationForTick = max(0f, min(differenceToMaxSpeed, maxAccelerationForTick)) * accelerationDirectionSign
        acceleratePlayerLinearly(playerEntity, effectiveAccelerationForTick)
    }

    private fun acceleratePlayerLinearly(playerEntity: PlayerEntity, acceleration: Float) {
        if (acceleration != 0f) {
            logger.debug { "Accelerating player linearly with $acceleration" }
            playerEntity.body.value.applyLinearImpulse(Vector2(acceleration * playerEntity.body.value.mass, 0f), playerEntity.position.vector, true)
        }
    }

    private fun acceleratePlayerAngularly(playerEntity: PlayerEntity, acceleration: Float) {
        if (acceleration != 0f) {
            logger.debug { "Accelerating player angularly with $acceleration" }
            playerEntity.body.value.applyAngularImpulse(acceleration * playerEntity.body.value.mass, true)
        }
    }
}
