package com.jafleck.game.maploading

import com.jafleck.extensions.libgdx.map.PatchedTmxMapLoader
import com.jafleck.game.util.files.AssetsFileHandleResolver
import com.jafleck.game.util.logger


class MapLoader(
    private val assetsFileHandleResolver: AssetsFileHandleResolver,
    private val mapEntitiesLoader: MapEntitiesLoader
) {
    companion object {
        internal const val MAP_ASSETS_DIRECTORY = "maps"
    }

    private val logger = logger(this::class)

    fun loadMap(map: GameMap) {
        logger.debug { "Starting to load map ${map.path}" }
        val tmxMapLoader = PatchedTmxMapLoader(assetsFileHandleResolver)
        val tiledMap = tmxMapLoader.load("$MAP_ASSETS_DIRECTORY/${map.name}")

        mapEntitiesLoader.loadMapEntities(map, tiledMap)
        logger.debug { "Finished loading the map" }
    }
}
