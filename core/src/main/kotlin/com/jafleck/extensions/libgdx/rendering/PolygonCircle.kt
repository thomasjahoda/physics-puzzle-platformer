package com.jafleck.extensions.libgdx.rendering

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import kotlin.math.max


fun calculateRecommendedCircleSegmentsForRendering(radius: Float, camera: OrthographicCamera): Int {
    val relevantRadius = radius / camera.combined.scaleX
    return calculateRecommendedCircleSegments(relevantRadius)
}

fun calculateRecommendedCircleSegmentsForPhysics(radius: Float): Int {
    val relevantRadius = radius / 0.1f
    return calculateRecommendedCircleSegments(relevantRadius)
}

private fun calculateRecommendedCircleSegments(relevantRadius: Float) =
    max(1, (6 * Math.cbrt(12 * relevantRadius.toDouble()).toFloat()).toInt())


private val tmpVector = Vector2()

/**
 * @return vertices
 */
fun createCirclePolygon(radius: Float, segments: Int): FloatArray {
    val rotationDegreesStep = (360f / segments)
    val vertices = FloatArray(segments * 2)

    tmpVector.set(0f, radius) // start at top
    vertices[0] = tmpVector.x
    vertices[1] = tmpVector.y
    for (segmentIndex in 1 until segments) {
        tmpVector.rotateAround(Vector2.Zero, rotationDegreesStep)
        vertices[segmentIndex * 2] = tmpVector.x
        vertices[segmentIndex * 2 + 1] = tmpVector.y
    }

    return vertices
}
