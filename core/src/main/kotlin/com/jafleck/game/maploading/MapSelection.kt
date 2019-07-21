package com.jafleck.game.maploading

import com.badlogic.gdx.Preferences
import com.jafleck.extensions.kotlin.lazyReadWrite
import com.jafleck.game.util.logger

class MapSelection(
    private val preferences: Preferences,
    private val mapList: MapList
) {
    private val logger = logger(this::class)

    private var actualSelectedMap by lazyReadWrite {
        val selectedMapPath = preferences.selectedMapPath
        if (selectedMapPath != null) {
            mapList.maps.find { it.path == selectedMapPath } ?: {
                val firstMap = mapList.maps[0]
                preferences.selectedMapPath = firstMap.path
                preferences.flush()
                firstMap
            }()
        } else {
            mapList.maps[0]
        }
    }

    var selectedMap: Map
        get() = actualSelectedMap
        set(value) {
            logger.debug { "Selecting map $value (currently selected: $actualSelectedMap)" }
            actualSelectedMap = value
            preferences.selectedMapPath = value.path
            preferences.flush()
        }
}

var Preferences.selectedMapPath: String?
    get() = getString("selectedMap")
    set(value) {
        putString("selectedMap", value)
    }
