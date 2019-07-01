package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class VelocityComponent(
    val vector: Vector2
) : Component {
    constructor(width: Float, height: Float) : this(Vector2(width, height))

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
