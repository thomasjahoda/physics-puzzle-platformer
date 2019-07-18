package com.jafleck.extensions.libgdx.rendering

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.math.RectanglePolygon


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
    val segments = calculateRecommendedCircleSegmentsForRendering(radius, camera)
    circle(originPosition.x, originPosition.y, radius, segments)
}

fun ShapeRenderer.triangle(originPosition: Vector2, vertices: FloatArray, verticesIndexOffset: Int) {
    val originX = originPosition.x
    val originY = originPosition.y
    triangle(originX + vertices[verticesIndexOffset + 0], originY + vertices[verticesIndexOffset + 1],
        originX + vertices[verticesIndexOffset + 2], originY + vertices[verticesIndexOffset + 3],
        originX + vertices[verticesIndexOffset + 4], originY + vertices[verticesIndexOffset + 5])
}

fun ShapeRenderer.triangles(originPosition: Vector2, triangleVertices: FloatArray) {
    for (i in 0 until triangleVertices.size step 2 * 3) {
        triangle(originPosition, triangleVertices, i)
    }
}

