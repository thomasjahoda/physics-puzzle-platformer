package com.jafleck.game.components.visual

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Polygon
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class TriangulatedVisualPolygonComponent(
    val trianglePolygonRelativeToOriginIncludingRotation: Polygon,
    val innerTrianglePolygonRelativeToOriginIncludingRotation: Polygon?
) : Component {

    fun setRotationDegrees(rotationDegrees: Float) {
        if (trianglePolygonRelativeToOriginIncludingRotation.rotation != rotationDegrees) {
            trianglePolygonRelativeToOriginIncludingRotation.rotation = rotationDegrees
            innerTrianglePolygonRelativeToOriginIncludingRotation?.rotation = rotationDegrees
        }
    }

    fun getTriangleVertices(): FloatArray {
        return trianglePolygonRelativeToOriginIncludingRotation.transformedVertices
    }

    fun getInnerTriangleVertices(): FloatArray? {
        return innerTrianglePolygonRelativeToOriginIncludingRotation?.transformedVertices
    }

    companion object : ComponentMapperAccessor<TriangulatedVisualPolygonComponent>(TriangulatedVisualPolygonComponent::class)
}
