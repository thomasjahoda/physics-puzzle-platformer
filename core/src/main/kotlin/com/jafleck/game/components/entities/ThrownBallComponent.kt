package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class ThrownBallComponent : Component {

    companion object : ComponentMapperAccessor<ThrownBallComponent>(ThrownBallComponent::class)
}
