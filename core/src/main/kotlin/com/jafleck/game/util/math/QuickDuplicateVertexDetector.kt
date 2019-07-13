package com.jafleck.game.util.math

import com.badlogic.gdx.math.Vector2

object QuickDuplicateVertexDetector {
    private val vertexHashes: com.badlogic.gdx.utils.FloatArray = com.badlogic.gdx.utils.FloatArray()

    fun getFirstDuplicate(vertices: FloatArray): Vector2? {
        // note potential performance-improvement: use some actual algorithm and not whatever I came up with here
        // i guess it is faster than some hash-set for normal cases because no extra memory has to be allocated but I think there should be something better

        val vertexCount = vertices.size / 2
        vertexHashes.ensureCapacity(vertexCount)
        vertexHashes.setSize(vertexCount)
        for (i in 0 until vertexCount) {
            vertexHashes[i] = hash(vertices, i * 2)
        }
        processDuplicateHashes { duplicateHash ->
            for (i in 0 until vertices.size step 2) {
                if (hash(vertices, i) == duplicateHash) {
                    val testX = vertices[i]
                    val testY = vertices[i + 1]
                    for (j in i + 2 until vertices.size step 2) {
                        if (testX == vertices[j] && testY == vertices[j + 1]) {
                            return Vector2(testX, testY)
                        }
                    }
                }
            }
        }
        return null
    }

    private inline fun processDuplicateHashes(block: (Float) -> Unit) {
        QuickSort.sortArrayPart(vertexHashes.items, 0, vertexHashes.size - 1)
        var previousHash = vertexHashes[0]
        for (i in 1 until vertexHashes.size) {
            val hash = vertexHashes[i]
            if (hash == previousHash) {
                block(hash)
            }
            previousHash = hash
        }
    }

    private fun hash(vertices: FloatArray, verticesIndex: Int): Float {
        val prime1 = 31
        val prime2 = 7
        return prime1 * (prime1 + vertices[verticesIndex]) * (vertices[verticesIndex + 1] + prime2)
    }
}
