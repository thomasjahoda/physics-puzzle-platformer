package com.jafleck.extensions.libgdx.math

import com.badlogic.gdx.math.Polygon


/**
 * Sets the vertices to the rectangle coordinates around the position (0/0).
 */
fun Polygon.setRectangleShapeAround00(width: Float, height: Float) {
    val vertices = vertices
    val halfWidth = width / 2
    val halfHeight = height / 2
    // bottom-left
    vertices[0] = -halfWidth
    vertices[1] = -halfHeight
    // top-left
    vertices[2] = -halfWidth
    vertices[3] = halfHeight
    // top-right
    vertices[4] = halfWidth
    vertices[5] = halfHeight
    // bottom-right
    vertices[6] = halfWidth
    vertices[7] = -halfHeight
}
