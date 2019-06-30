package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class VelocityComponent(
    x: Float,
    y: Float
) : Component {

    companion object : ComponentMapperAccessor<VelocityComponent>(VelocityComponent::class)

    val vector = Vector2(x, y)

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
}
