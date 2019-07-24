package com.jafleck.game.util.libgdx.maps

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2

val TiledMap.sizeInTiles
    get() = Vector2((properties["width"] as Int).toFloat(), (properties["height"] as Int).toFloat())
val TiledMap.infinite: Boolean
    get() = (properties["infinite"] as Int? ?: 0) == 1
