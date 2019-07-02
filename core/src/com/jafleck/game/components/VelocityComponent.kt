package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

/**
 * Velocity of the entity.
 * Entities with this component shall only be either dynamic or kinematic.
 * Entities without this component shall only be static bodies.
 */
class VelocityComponent(
    val vector: Vector2
) : Component {
    constructor(x: Float, y: Float) : this(Vector2(x, y))

    var x
        get() = vector.x
        set(value) {
            vector.x = value
        }
    var y
        get() = vector.y
        set(value) {
            vector.y = value
        }

    companion object : ComponentMapperAccessor<VelocityComponent>(VelocityComponent::class)
}
