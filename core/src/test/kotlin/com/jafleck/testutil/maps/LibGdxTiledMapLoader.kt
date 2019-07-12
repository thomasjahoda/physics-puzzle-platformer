package com.jafleck.testutil.maps

import com.badlogic.gdx.maps.tiled.TiledMap
import com.jafleck.extensions.libgdx.map.PatchedTmxMapLoader
import com.jafleck.game.maploading.MapLoader
import com.jafleck.testutil.CustomClasspathAssetsFileHandleResolver

class LibGdxTiledMapLoader {
    fun loadMap(map: String): TiledMap {
        val patchedTmxMapLoader = PatchedTmxMapLoader(CustomClasspathAssetsFileHandleResolver())
        return patchedTmxMapLoader.load("${MapLoader.MAP_ASSETS_DIRECTORY}/$map")
    }
}
