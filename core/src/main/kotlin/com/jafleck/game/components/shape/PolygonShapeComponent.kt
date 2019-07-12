package com.jafleck.game.components.shape

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

@Suppress("ArrayInDataClass") // true equality and hash doesn't matter here, the JVM-impl is faster and enough
data class PolygonShapeComponent(
    val vertices: FloatArray
) : Component, ShapeComponent {
    private var boundingRectangle: Vector2 = calculateBoundingRectangle()
    private var dirty = false

    override fun getRectangleAroundShape(target: Vector2): Vector2 {
        if (dirty) {
            boundingRectangle = calculateBoundingRectangle()
            dirty = false
        }
        return boundingRectangle
    }

    fun markAsDirty() {
        dirty = true
    }

    private fun calculateBoundingRectangle() = Polygon(vertices).boundingRectangle.getSize(Vector2())

    companion object : ComponentMapperAccessor<PolygonShapeComponent>(PolygonShapeComponent::class)
}
