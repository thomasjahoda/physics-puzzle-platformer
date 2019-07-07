package com.jafleck.game.gameplay.systems

import com.badlogic.gdx.math.Vector2
import com.jafleck.game.components.PlayerMovementState
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.util.systems.PlayerEntitySystem


class PlayerMovementSystem(
    priority: Int
) : PlayerEntitySystem(priority) {

    private val acceleration = 1f

    override fun processPlayer(playerEntity: PlayerEntity) {
        when (playerEntity.player.movementState) {
            PlayerMovementState.LEFT -> playerEntity.body.value.applyLinearImpulse(Vector2(-acceleration, 0f), playerEntity.position.vector, true)
            PlayerMovementState.RIGHT -> playerEntity.body.value.applyLinearImpulse(Vector2(acceleration, 0f), playerEntity.position.vector, true)
            PlayerMovementState.NONE -> {
                // breaking automatically
            }
        }
    }
}
