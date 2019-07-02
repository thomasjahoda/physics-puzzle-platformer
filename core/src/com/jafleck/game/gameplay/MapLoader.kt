package com.jafleck.game.gameplay

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.entities.PlatformEntityCreator
import com.jafleck.game.entities.PlayerEntityCreator

class MapLoader(
    private val playerEntityCreator: PlayerEntityCreator,
    private val platformEntityCreator: PlatformEntityCreator
) {
    fun loadMap() {
        // hard-coded level
        // TODO load level from file

        // TODO use world-coordinates in meters instead of pixels
        playerEntityCreator.createPlayerEntity(Vector2(1f, 1f))

        platformEntityCreator.createPlatformEntity(Rectangle(0f, 0f, 5f, 0.5f))
        platformEntityCreator.createPlatformEntity(Rectangle(3f, 0.5f, 0.5f, 2f))

        platformEntityCreator.createPlatformEntity(Rectangle(0f, 0f, 5f, 0.5f))
        platformEntityCreator.createPlatformEntity(Rectangle(-5f, -5f, 5f, 5f))
        platformEntityCreator.createPlatformEntity(Rectangle(0f, -5f, 1f, 1f))
    }
}
