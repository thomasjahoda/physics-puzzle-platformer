package com.jafleck.game.components.shape

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class CircleShapeComponent(
    val radius: Float
) : Component, ShapeComponent {

    companion object : ComponentMapperAccessor<CircleShapeComponent>(CircleShapeComponent::class)
}
