package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class PlayerComponent : Component {

    companion object : ComponentMapperAccessor<PlayerComponent>(PlayerComponent::class)
}
