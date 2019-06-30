package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class PlayerComponent(
    val name: String
) : Component {

    companion object : ComponentMapperAccessor<PlayerComponent>(PlayerComponent::class)
}
