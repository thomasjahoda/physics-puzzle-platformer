package com.jafleck.game.components.shape

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class CircleShapeComponent(
    val radius: Float
) : Component, ShapeComponent {

    override fun getRectangleAroundShape(target: Vector2): Vector2 {
        return target.set(2 * radius, 2 * radius)
    }

    companion object : ComponentMapperAccessor<CircleShapeComponent>(CircleShapeComponent::class)
}
