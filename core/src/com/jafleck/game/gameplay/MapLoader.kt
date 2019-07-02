package com.jafleck.game.gameplay

import com.badlogic.gdx.Gdx
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
        playerEntityCreator.createPlayerEntity(Vector2(100f, 200f))
        platformEntityCreator.createPlatformEntity(Rectangle(0f, 50f, Gdx.graphics.width.toFloat(), 50f))
        platformEntityCreator.createPlatformEntity(Rectangle(300f, 100f, 50f, 200f))
    }
}
