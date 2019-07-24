package com.jafleck.testutil.maps

import com.badlogic.gdx.maps.tiled.TiledMap
import com.jafleck.extensions.libgdx.maps.PatchedTmxMapLoader
import com.jafleck.game.maploading.GameMap
import com.jafleck.game.maploading.MapLoader
import com.jafleck.testutil.CustomClasspathAssetsFileHandleResolver

object LibGdxTiledMapLoader {
    fun loadMap(gameMap: GameMap): TiledMap {
        return loadMap(gameMap.path)
    }

    fun loadMap(map: String): TiledMap {
        val patchedTmxMapLoader = PatchedTmxMapLoader(CustomClasspathAssetsFileHandleResolver())
        return patchedTmxMapLoader.load("${MapLoader.MAP_ASSETS_DIRECTORY}/$map")
    }
}
