package com.jafleck.game.gameplay.controlandmainphases

import com.jafleck.game.maploading.GameMapList
import com.jafleck.game.maploading.MapSelection
import com.jafleck.game.util.logger

class FinishedMapSuccessfullyHandler(
    private val mapSelection: MapSelection,
    private val mapList: GameMapList,
    private val postSystemUpdatePhaseActionExecutor: PostSystemUpdatePhaseActionExecutor
) {

    private val logger = logger(this::class)

    fun onMapSuccessfullyFinished() {
        postSystemUpdatePhaseActionExecutor.executeActionAfterTheCurrentTick {
            selectNextMap()
        }
    }

    private fun selectNextMap() {
        logger.debug { "Selecting next map" }
        var currentMapIndex = mapList.maps.indexOf(mapSelection.selectedMap)
        if (mapList.maps.size == currentMapIndex + 1) {
            // the current map is the last map
            currentMapIndex = 0
        } else {
            currentMapIndex++
        }
        mapSelection.selectedMap = mapList.maps[currentMapIndex]
    }
}
