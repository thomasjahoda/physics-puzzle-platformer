package com.jafleck.extensions.libgdx.math

import com.badlogic.gdx.math.Vector2

fun List<Vector2>.toFloatArray(): FloatArray {
    val floatArray = FloatArray(this.size * 2)
    for (i in 0 until this.size) {
        floatArray[i * 2] = this[i].x
        floatArray[i * 2 + 1] = this[i].y
    }
    return floatArray
}

class VerticesBuilder {
    private val vertices = mutableListOf<Vector2>()

    fun p(x: Float, y: Float): VerticesBuilder {
        vertices.add(Vector2(x, y))
        return this
    }

    fun p(x: Number, y: Number): VerticesBuilder {
        vertices.add(Vector2(x.toFloat(), y.toFloat()))
        return this
    }

    fun asFloatArray(): FloatArray {
        return vertices.toFloatArray()
    }

    fun asListOfVertices(): MutableList<Vector2> {
        return vertices
    }
}

fun FloatArray.toListOfVertices(): List<Vector2> {
    require(this.size.rem(2) == 0) { "must be array with even amount of values because they need x,y pairs" }
    val vertices = ArrayList<Vector2>(this.size / 2)
    for (i in 0 until size step 2) {
        vertices.add(Vector2(this[i], this[i + 1]))
    }
    return vertices
}

fun buildVertices(block: VerticesBuilder.() -> Unit): VerticesBuilder {
    val builder = VerticesBuilder()
    builder.block()
    return builder
}
