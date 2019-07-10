package com.jafleck.extensions.libgdx.math

import com.badlogic.gdx.math.Polygon


inline class RectanglePolygon(val polygon: Polygon) {

    companion object {
        fun create(): RectanglePolygon {
            return RectanglePolygon(Polygon(FloatArray(4 * 2)))
        }
    }

    fun setRectangleShapeAround00(width: Float, height: Float) {
        val vertices = this.vertices
        val halfWidth = width / 2
        val halfHeight = height / 2
        // bottom-left
        vertices.bottomLeftX = -halfWidth
        vertices.bottomLeftY = -halfHeight
        // top-left
        vertices.topLeftX = -halfWidth
        vertices.topLeftY = halfHeight
        // top-right
        vertices.topRightX = halfWidth
        vertices.topRightY = halfHeight
        // bottom-right
        vertices.bottomRightX = halfWidth
        vertices.bottomRightY = -halfHeight
    }

    fun setRectangleShapeFromTopLeftCorner(width: Float, height: Float) {
        val vertices = this.vertices
        // bottom-left
        vertices.bottomLeftX = 0f
        vertices.bottomLeftY = -height
        // top-left
        vertices.topLeftX = 0f
        vertices.topLeftY = 0f
        // top-right
        vertices.topRightX = width
        vertices.topRightY = 0f
        // bottom-right
        vertices.bottomRightX = width
        vertices.bottomRightY = -height
    }

    inline var vertices: RectanglePolygonVertices
        get() = RectanglePolygonVertices(polygon.vertices)
        set(value) {
            polygon.vertices = value.vertices
        }

    inline val transformedVertices: RectanglePolygonVertices
        get() = RectanglePolygonVertices(polygon.transformedVertices)
}

inline class RectanglePolygonVertices(val vertices: FloatArray) {
    inline var bottomLeftX: Float
        get() = vertices[0]
        set(value) {
            vertices[0] = value
        }
    inline var bottomLeftY: Float
        get() = vertices[1]
        set(value) {
            vertices[1] = value
        }
    inline var topLeftX: Float
        get() = vertices[2]
        set(value) {
            vertices[2] = value
        }
    inline var topLeftY: Float
        get() = vertices[3]
        set(value) {
            vertices[3] = value
        }
    inline var topRightX: Float
        get() = vertices[4]
        set(value) {
            vertices[4] = value
        }
    inline var topRightY: Float
        get() = vertices[5]
        set(value) {
            vertices[5] = value
        }
    inline var bottomRightX: Float
        get() = vertices[6]
        set(value) {
            vertices[6] = value
        }
    inline var bottomRightY: Float
        get() = vertices[7]
        set(value) {
            vertices[7] = value
        }
}
