package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

/**
 * Origin (center) position of entity in the world.
 */
data class OriginPositionComponent(
    val vector: Vector2
) : Component {
    constructor(x: Float, y: Float) : this(Vector2(x, y))

    var originX
        get() = vector.x
        set(value) {
            vector.x = value
        }
    var originY
        get() = vector.y
        set(value) {
            vector.y = value
        }

    companion object : ComponentMapperAccessor<OriginPositionComponent>(OriginPositionComponent::class)
}
