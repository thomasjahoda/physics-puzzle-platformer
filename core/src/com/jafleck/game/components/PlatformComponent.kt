package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class PlatformComponent : Component {

    companion object : ComponentMapperAccessor<PlatformComponent>(PlatformComponent::class)
}
