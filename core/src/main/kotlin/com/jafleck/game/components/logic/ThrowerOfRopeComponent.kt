package com.jafleck.game.components.logic

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.entities.ThrownRopeEntity

class ThrowerOfRopeComponent(val thrownRope: ThrownRopeEntity) : Component {

    companion object : ComponentMapperAccessor<ThrowerOfRopeComponent>(ThrowerOfRopeComponent::class)
}
