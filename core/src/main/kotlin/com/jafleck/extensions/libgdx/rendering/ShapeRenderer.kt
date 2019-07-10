package com.jafleck.extensions.libgdx.rendering

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import kotlin.math.max


fun ShapeRenderer.drawRectanglePolygon(rectanglePolygon: Polygon) {
    val transformedVertices = rectanglePolygon.transformedVertices
    triangle(transformedVertices[0], transformedVertices[1], transformedVertices[2], transformedVertices[3], transformedVertices[4], transformedVertices[5])
    triangle(transformedVertices[0], transformedVertices[1], transformedVertices[4], transformedVertices[5], transformedVertices[6], transformedVertices[7])
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

