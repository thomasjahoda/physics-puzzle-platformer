package com.jafleck.extensions.libgdx.rendering

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.math.RectanglePolygon
import kotlin.math.max


fun ShapeRenderer.fillRectanglePolygon(rectanglePolygon: RectanglePolygon) {
    val transformedVertices = rectanglePolygon.transformedVertices
    triangle(
        transformedVertices.bottomLeftX, transformedVertices.bottomLeftY,
        transformedVertices.topLeftX, transformedVertices.topLeftY,
        transformedVertices.topRightX, transformedVertices.topRightY)
    triangle(
        transformedVertices.bottomLeftX, transformedVertices.bottomLeftY,
        transformedVertices.topRightX, transformedVertices.topRightY,
        transformedVertices.bottomRightX, transformedVertices.bottomRightY)
}

fun ShapeRenderer.box(originPosition: Vector2, rectangleShape: Vector2) {
    box(originPosition.x - rectangleShape.x / 2, originPosition.y - rectangleShape.y / 2, 0f,
        rectangleShape.x, rectangleShape.y, 0f)
}

fun ShapeRenderer.box(originPosition: Vector2, rectangleShape: Vector2, borderThickness: Float) {
    box(originPosition.x - rectangleShape.x / 2 + borderThickness, originPosition.y - rectangleShape.y / 2 + borderThickness, 0f,
        rectangleShape.x - 2 * borderThickness, rectangleShape.y - 2 * borderThickness, 0f)
}

fun ShapeRenderer.circle(originPosition: Vector2, radius: Float, camera: OrthographicCamera) {
    val segments = max(1, (6 * Math.cbrt(12 * (radius / camera.combined.scaleX).toDouble()).toFloat()).toInt())
    circle(originPosition.x, originPosition.y, radius, segments)
}

fun ShapeRenderer.triangle(originPosition: Vector2, vertices: FloatArray) {
    val originX = originPosition.x
    val originY = originPosition.y
    triangle(originX + vertices[0], originY + vertices[1],
        originX + vertices[2], originY + vertices[3],
        originX + vertices[4], originY + vertices[5])
}

