package com.jafleck.game.maploading

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle
import com.jafleck.game.entities.DeathZoneEntityCreator
import com.jafleck.game.util.libgdx.maps.infinite
import com.jafleck.game.util.libgdx.maps.sizeInTiles
import com.jafleck.game.util.logger


class MapDeathBorderLoader(
    private val deathZoneEntityCreator: DeathZoneEntityCreator
) {
    private val logger = logger(this::class)

    companion object {
        internal const val DEATH_AREA_THICKNESS = 1f
    }

    fun createInvisibleDeathBorder(tiledMap: TiledMap) {
        require(tiledMap.infinite.not()) {
            "Infinite maps are not supported because a death border is created for every map. " +
                "(if you want you can just add a retun here to support infinite maps)"
        }
        val worldSize = tiledMap.sizeInTiles

        /*
         * ----------------
         * |--------------|
         * | |          | |
         * | |          | |
         * |--------------|
         * ----------------
         */
        // up and down
        deathZoneEntityCreator.createInvisibleDeathZone(Rectangle(-DEATH_AREA_THICKNESS, -DEATH_AREA_THICKNESS, worldSize.x + 2 * DEATH_AREA_THICKNESS, DEATH_AREA_THICKNESS))
        deathZoneEntityCreator.createInvisibleDeathZone(Rectangle(-DEATH_AREA_THICKNESS, worldSize.y, worldSize.x + 2 * DEATH_AREA_THICKNESS, DEATH_AREA_THICKNESS))
        // left and right
        deathZoneEntityCreator.createInvisibleDeathZone(Rectangle(-DEATH_AREA_THICKNESS, 0f, DEATH_AREA_THICKNESS, worldSize.y))
        deathZoneEntityCreator.createInvisibleDeathZone(Rectangle(worldSize.x, 0f, DEATH_AREA_THICKNESS, worldSize.y))
    }
}
