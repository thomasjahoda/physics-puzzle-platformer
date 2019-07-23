package com.jafleck.game.maploading

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.jafleck.extensions.libgdx.map.id
import com.jafleck.game.components.map.ActiveGameMapComponent
import com.jafleck.game.entities.ActiveGameMapEntity
import com.jafleck.game.util.libgdx.map.entityType
import com.jafleck.game.util.logger


class MapEntitiesLoader(
    private val mapEntityLoaderLocator: MapEntityLoaderLocator,
    private val engine: Engine
) {
    private val logger = logger(this::class)

    fun loadMapEntities(map: GameMap, tiledMap: TiledMap) {
        val allMapObjects = tiledMap.layers.flatMap { it.objects }
        logger.debug { "Loading ${allMapObjects.size} map objects" }

        validateMapWasUnloaded()
        val mapEntity = createActiveGameMapEntity(map, tiledMap)
        allMapObjects.forEach(::loadMapObjectAsEntityIfPossible)
        mapEntity.activeGameMap.fullyLoaded = true

        logger.debug { "Finished loading the map entities" }
    }

    private fun validateMapWasUnloaded() {
        require(engine.entities.size() == 0) { "${MapUnloader::class.simpleName} has to be called first to unload the map" }
    }

    private fun createActiveGameMapEntity(map: GameMap, tiledMap: TiledMap): ActiveGameMapEntity {
        return engine.createEntity().apply {
            add(ActiveGameMapComponent(map, tiledMap, fullyLoaded = false))
        }.let { ActiveGameMapEntity(it) }
    }

    private fun loadMapObjectAsEntityIfPossible(mapObject: MapObject) {
        val entityType = mapObject.entityType
        if (entityType != null) {
            logger.debug { "Loading entity map object ${mapObject.id}" }
            val mapEntityLoader = mapEntityLoaderLocator.getMapEntityLoader(entityType)
            mapEntityLoader.loadEntity(mapObject)
        } else {
            logger.debug { "Skipping map object ${mapObject.id} because no entity type was specified" }
        }
    }
}
