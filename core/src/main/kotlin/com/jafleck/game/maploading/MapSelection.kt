package com.jafleck.game.maploading

import com.badlogic.gdx.Preferences
import com.jafleck.game.util.listeners.PropertyChangeListenerMultiplexer
import com.jafleck.game.util.listeners.observableByListeners
import com.jafleck.game.util.logger

class MapSelection(
    private val preferences: Preferences,
    private val gameMapList: GameMapList,
    private val mapUnloader: MapUnloader,
    private val mapLoader: MapLoader
) {
    private val logger = logger(this::class)

    val selectedMapListeners = PropertyChangeListenerMultiplexer<GameMap>()
    var selectedMap by observableByListeners(getInitialSelectedMap(), selectedMapListeners)

    init {
        selectedMapListeners.addNewValueListener {
            persistMapSelection(it)
        }

        selectedMapListeners.addNewValueListener {
            mapUnloader.unloadMap()
            mapLoader.loadMap(it)
        }
    }

    private fun getInitialSelectedMap(): GameMap {
        val selectedMapPath = preferences.selectedMapPath
        return if (selectedMapPath != null) {
            gameMapList.maps.find { it.path == selectedMapPath } ?: {
                val firstMap = gameMapList.maps[0]
                persistMapSelection(firstMap)
                firstMap
            }()
        } else {
            gameMapList.maps[0]
        }
    }

    private fun persistMapSelection(new: GameMap) {
        logger.debug { "Persisting map selection $new" }
        preferences.selectedMapPath = new.path
        preferences.flush()
    }
}

var Preferences.selectedMapPath: String?
    get() = getString("selectedMap")
    set(value) {
        putString("selectedMap", value)
    }
