package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class RopeComponent(
    /**
     * First element is first/nearest rope part as seen from thrower or first attachment position.
     */
    val parts: MutableList<Entity>
) : Component {

    companion object : ComponentMapperAccessor<RopeComponent>(RopeComponent::class)
}
