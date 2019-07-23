package com.jafleck.game.components.map

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.maps.tiled.TiledMap
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.maploading.GameMap


data class ActiveGameMapComponent(
    val value: GameMap,
    val tiledMap: TiledMap,
    var fullyLoaded: Boolean
) : Component {

    companion object : ComponentMapperAccessor<ActiveGameMapComponent>(ActiveGameMapComponent::class)
}
