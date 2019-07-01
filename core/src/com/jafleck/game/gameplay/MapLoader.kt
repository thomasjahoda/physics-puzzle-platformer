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
        playerEntityCreator.createPlayerEntity(Vector2(100f, 200f))
        platformEntityCreator.createPlatformEntity(Rectangle(0f, 0f, 500f, 50f))
        platformEntityCreator.createPlatformEntity(Rectangle(300f, 50f, 50f, 200f))
    }
}
