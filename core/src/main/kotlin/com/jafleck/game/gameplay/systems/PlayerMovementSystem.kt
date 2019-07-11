package com.jafleck.game.gameplay.systems

import com.badlogic.gdx.math.Vector2
import com.jafleck.game.components.PlayerMovementState
import com.jafleck.game.entities.PlayerEntity


class PlayerMovementSystem : PlayerEntitySystem() {

    private val acceleration = 0.25f

    override fun processPlayer(playerEntity: PlayerEntity) {
        when (playerEntity.player.movementState) {
            PlayerMovementState.LEFT -> acceleratePlayer(playerEntity, -acceleration)
            PlayerMovementState.RIGHT -> acceleratePlayer(playerEntity, acceleration)
            PlayerMovementState.NONE -> {
                // breaking automatically
            }
        }
    }

    private fun acceleratePlayer(playerEntity: PlayerEntity, acceleration: Float) {
        playerEntity.body.value.applyLinearImpulse(Vector2(acceleration * playerEntity.body.value.mass, 0f), playerEntity.position.vector, true)
    }
}
