package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class MapObjectComponent(
    val value: MapObject
) : Component {

    companion object : ComponentMapperAccessor<MapObjectComponent>(MapObjectComponent::class)
}
