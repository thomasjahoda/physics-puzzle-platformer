package com.jafleck.game.components.visual

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Polygon
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class TriangulatedVisualPolygonComponent(
    val trianglePolygonRelativeToOriginIncludingRotation: Polygon
) : Component {

    fun setRotationDegrees(rotationDegrees: Float) {
        if (trianglePolygonRelativeToOriginIncludingRotation.rotation != rotationDegrees) {
            trianglePolygonRelativeToOriginIncludingRotation.rotation = rotationDegrees
        }
    }

    fun getTriangleVertices(): FloatArray {
        return trianglePolygonRelativeToOriginIncludingRotation.transformedVertices
    }

    companion object : ComponentMapperAccessor<TriangulatedVisualPolygonComponent>(TriangulatedVisualPolygonComponent::class)
}
