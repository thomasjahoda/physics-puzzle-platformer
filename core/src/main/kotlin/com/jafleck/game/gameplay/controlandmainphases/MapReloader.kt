package com.jafleck.game.gameplay.controlandmainphases

import com.badlogic.ashley.core.Engine
import com.jafleck.game.entities.ActiveGameMapEntity
import com.jafleck.game.maploading.MapEntitiesLoader
import com.jafleck.game.maploading.MapUnloader
import com.jafleck.game.util.logger

class MapReloader(
    private val mapUnloader: MapUnloader,
    private val engine: Engine,
    private val mapEntitiesLoader: MapEntitiesLoader,
    private val postSystemUpdatePhaseActionExecutor: PostSystemUpdatePhaseActionExecutor
) {

    private val logger = logger(this::class)

    fun reloadMapAfterSystemProcessing() {
        postSystemUpdatePhaseActionExecutor.executeActionAfterTheCurrentTick {
            reloadMap()
        }
    }

    private fun reloadMap() {
        logger.debug { "Reloading current map" }
        val currentMapEntity = ActiveGameMapEntity.getCurrentMap(engine)!!
        val (gameMap, tiledMap, fullyLoaded) = currentMapEntity.activeGameMap
        require(fullyLoaded)
        mapUnloader.unloadMap()
        mapEntitiesLoader.loadMapEntities(gameMap, tiledMap)
    }
}
