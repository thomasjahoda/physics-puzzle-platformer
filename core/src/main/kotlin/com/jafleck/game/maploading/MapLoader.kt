package com.jafleck.game.maploading

import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.jafleck.game.util.files.AssetsFileHandleResolver
import com.jafleck.game.util.logger


class MapLoader(
    private val assetsFileHandleResolver: AssetsFileHandleResolver,
    private val mapEntityLoaderLocator: MapEntityLoaderLocator
) {
    companion object {
        const val MAP_ASSETS_DIRECTORY = "maps"
    }

    private val logger = logger(this::class)

    fun loadMap(name: String) {
        val map = TmxMapLoader(assetsFileHandleResolver).load("$MAP_ASSETS_DIRECTORY/$name")
        val allMapObjects = map.layers.flatMap { it.objects }
        logger.debug { "Loading ${allMapObjects.size} map objects" }

        allMapObjects.forEach {
            val mapEntityLoader = mapEntityLoaderLocator.getMapEntityLoader(it.properties["type"] as String)
            mapEntityLoader.loadEntity(it)
        }
    }
}
