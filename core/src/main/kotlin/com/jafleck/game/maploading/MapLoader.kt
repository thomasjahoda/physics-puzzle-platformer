package com.jafleck.game.maploading

import com.jafleck.extensions.libgdx.map.PatchedTmxMapLoader
import com.jafleck.extensions.libgdx.map.id
import com.jafleck.game.util.files.AssetsFileHandleResolver
import com.jafleck.game.util.libgdx.map.entityType
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
        val map = PatchedTmxMapLoader(assetsFileHandleResolver).load("$MAP_ASSETS_DIRECTORY/$name")
        val allMapObjects = map.layers.flatMap { it.objects }
        logger.debug { "Loading ${allMapObjects.size} map objects" }

        allMapObjects.forEach {
            val entityType = it.entityType
            if (entityType != null) {
                logger.debug { "Loading entity map object ${it.id}" }
                val mapEntityLoader = mapEntityLoaderLocator.getMapEntityLoader(entityType)
                mapEntityLoader.loadEntity(it)
            } else {
                logger.debug { "Skipping map object ${it.id} because no entity type was specified" }
            }
        }
    }
}
