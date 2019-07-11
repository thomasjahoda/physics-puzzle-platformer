package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdx.map.id
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class MapObjectComponent(
    val value: MapObject
) : Component {

    override fun toString(): String {
        return "MapObjectComponent for id '${value.id}'"
    }

    companion object : ComponentMapperAccessor<MapObjectComponent>(MapObjectComponent::class)
}
