package com.jafleck.game.util.math

import kotlin.math.sign

/**
 * Implemented by re-using internals from com.badlogic.gdx.math.EarClippingTriangulator.
 * @see com.badlogic.gdx.math.EarClippingTriangulator
 *
 * A potentially better solution would be https://stackoverflow.com/a/45372025/1218254, I guess.
 */
class PolygonTypeDetector {

    fun determinePolygonType(vertices: FloatArray): PolygonType {
        for (i in 0 until vertices.size step 2) {
            if (classifyVertex(vertices, i) == PolygonType.CONCAVE) return PolygonType.CONCAVE
        }
        return PolygonType.CONVEX
    }

    private fun classifyVertex(vertices: FloatArray, currentVertexIndex: Int): PolygonType {
        val previous = if (currentVertexIndex != 0) {
            currentVertexIndex - 2
        } else {
            vertices.size - 2
        }
        val next = if (currentVertexIndex != vertices.size - 2) {
            currentVertexIndex + 2
        } else {
            0
        }
        return computeSpannedAreaSign(
            vertices[previous], vertices[previous + 1],
            vertices[currentVertexIndex], vertices[currentVertexIndex + 1],
            vertices[next], vertices[next + 1]
        )
    }

    private fun computeSpannedAreaSign(p1x: Float, p1y: Float, p2x: Float, p2y: Float, p3x: Float, p3y: Float): PolygonType {
        var area = p1x * (p3y - p2y)
        area += p2x * (p1y - p3y)
        area += p3x * (p2y - p1y)
        return if (sign(area).toInt() == 1) PolygonType.CONVEX else PolygonType.CONCAVE
    }

}

enum class PolygonType {
    CONVEX,
    CONCAVE
}
