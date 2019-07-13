package com.jafleck.game.util.math

import com.badlogic.gdx.math.Vector2

object PolygonNotHavingVerticesOnSameLineValidator {
    fun getFirstVertexOnLineAsOther(vertices: FloatArray): Vector2? {
        var previousGradient = (vertices[1] - vertices[vertices.size - 1]) / (vertices[0] - vertices[vertices.size - 2])
        for (i in 0 until vertices.size - 2 step 2) {
            val gradient = gradientWithNext(vertices, i)
            if (previousGradient == gradient) {
                return Vector2(vertices[i], vertices[i + 1])
            }
            previousGradient = gradient
        }
        return null
    }

    private fun gradientWithNext(vertices: FloatArray, i: Int) = (vertices[i + 3] - vertices[i + 1]) / (vertices[i + 2] - vertices[i + 0])

}
