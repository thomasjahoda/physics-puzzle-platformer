package com.jafleck.game.components.shape

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class RectangleShapeComponent(
    val vector: Vector2
) : Component, ShapeComponent {
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

    override fun getRectangleAroundShape(target: Vector2): Vector2 {
        return target.set(vector)
    }

    companion object : ComponentMapperAccessor<RectangleShapeComponent>(RectangleShapeComponent::class)
}
