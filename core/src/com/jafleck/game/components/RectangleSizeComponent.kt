package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class RectangleSizeComponent(
    val vector: Vector2
) : Component {
    constructor(width: Float, height: Float) : this(Vector2(width, height))

    var width
        get() = vector.x
        set(value) {
            vector.x = value
        }
    var height
        get() = vector.y
        set(value) {
            vector.y = value
        }

    companion object : ComponentMapperAccessor<RectangleSizeComponent>(RectangleSizeComponent::class)
}
