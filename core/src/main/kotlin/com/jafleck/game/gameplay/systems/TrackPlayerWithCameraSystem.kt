package com.jafleck.game.gameplay.systems

import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.gameplay.ui.GameCamera


class TrackPlayerWithCameraSystem(
    private val camera: GameCamera
) : PlayerEntitySystem() {

    override fun processPlayer(playerEntity: PlayerEntity, deltaSeconds: Float) {
        camera.position.set(playerEntity.position.vector, camera.position.z)
        camera.update()
    }

}
