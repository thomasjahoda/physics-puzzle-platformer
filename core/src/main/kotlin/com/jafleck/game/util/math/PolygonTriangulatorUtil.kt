package com.jafleck.game.util.math

import com.badlogic.gdx.math.EarClippingTriangulator
import com.badlogic.gdx.utils.ShortArray
import kotlin.math.max

val tmpTriangleVertices = FloatArray(3 * 2)

inline fun triangulate(polygonVertices: FloatArray, polygonTriangulator: EarClippingTriangulator, triangleVerticesConsumer: (FloatArray) -> Unit) {
    val triangleIndices = polygonTriangulator.computeTriangles(polygonVertices)
    for (triangleIndex in 0 until triangleIndices.size / 3) {
        copySelectedVerticesOfTriangleTo(polygonVertices, triangleIndices, triangleIndex, tmpTriangleVertices, 0)
        triangleVerticesConsumer(tmpTriangleVertices)
    }
}

/**
 * WARNING: expensive allocation! should be cached
 */
fun triangulate(polygonVertices: FloatArray, polygonTriangulator: EarClippingTriangulator): FloatArray {
    val triangleVertexIndices = polygonTriangulator.computeTriangles(polygonVertices)
    val polygonVertexCount = polygonVertices.size / 2
    val triangleCount = max(0, polygonVertexCount - 2) // from EarClippingTriangulator#computeTriangles when ensuring capacity
    val triangleVertices = FloatArray(triangleCount * 3 * 2)
    for (triangleIndex in 0 until triangleCount) {
        copySelectedVerticesOfTriangleTo(polygonVertices, triangleVertexIndices, triangleIndex, triangleVertices, triangleIndex * 6)
    }
    return triangleVertices
}

fun copySelectedVerticesOfTriangleTo(polygonVertices: FloatArray, triangleVertexIndicesReferencingPolygonVertices: ShortArray, triangleIndex: Int, targetTriangleVertices: FloatArray, targetIndexOffset: Int) {
    val baseIndex = triangleIndex * 3
    targetTriangleVertices[targetIndexOffset + 0] = polygonVertices[(triangleVertexIndicesReferencingPolygonVertices[baseIndex + 0].toInt()) * 2]
    targetTriangleVertices[targetIndexOffset + 1] = polygonVertices[(triangleVertexIndicesReferencingPolygonVertices[baseIndex + 0].toInt()) * 2 + 1]
    targetTriangleVertices[targetIndexOffset + 2] = polygonVertices[(triangleVertexIndicesReferencingPolygonVertices[baseIndex + 1].toInt()) * 2]
    targetTriangleVertices[targetIndexOffset + 3] = polygonVertices[(triangleVertexIndicesReferencingPolygonVertices[baseIndex + 1].toInt()) * 2 + 1]
    targetTriangleVertices[targetIndexOffset + 4] = polygonVertices[(triangleVertexIndicesReferencingPolygonVertices[baseIndex + 2].toInt()) * 2]
    targetTriangleVertices[targetIndexOffset + 5] = polygonVertices[(triangleVertexIndicesReferencingPolygonVertices[baseIndex + 2].toInt()) * 2 + 1]
}
