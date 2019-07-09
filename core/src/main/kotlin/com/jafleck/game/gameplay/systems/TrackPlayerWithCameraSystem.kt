package com.jafleck.game.gameplay.systems

import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.gameplay.ui.GameCamera


class TrackPlayerWithCameraSystem(
    priority: Int,
    private val camera: GameCamera
) : PlayerEntitySystem(priority) {

    override fun processPlayer(playerEntity: PlayerEntity) {
        camera.position.set(playerEntity.position.vector.cpy(), camera.position.z)
    }

}
