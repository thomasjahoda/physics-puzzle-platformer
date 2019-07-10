package com.jafleck.game.maploading

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.MapObject

interface MapEntityLoader {

    val type: String

    fun loadEntity(mapObject: MapObject): Entity?
}
