package com.jafleck.game.util.math

import com.jafleck.extensions.libgdx.math.buildVertices
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class PolygonTypeDetectorTest {

    @Test
    fun `triangle should return convex`() {
        val polygonVertices = buildVertices {
            p(0, 0)
            p(1, 1)
            p(1, 0)
        }.asFloatArray()

        val polygonType = PolygonTypeDetector().determinePolygonType(polygonVertices)

        Assertions.assertThat(polygonType).isEqualTo(PolygonType.CONVEX)
    }

    @Test
    fun `arrow should return concave`() {
        val polygonVertices = buildVertices {
            p(0, 0)
            p(0.5, 1)
            p(1, 0)
            p(0.5, 0.5) // in the middle of triangle
        }.asFloatArray()

        val polygonType = PolygonTypeDetector().determinePolygonType(polygonVertices)

        Assertions.assertThat(polygonType).isEqualTo(PolygonType.CONCAVE)
    }

    @Test
    fun `self-intersecting should return concave`() {
        val polygonVertices = buildVertices {
            p(0, 0)
            p(0.5, 1)
            p(1, 0)
            p(0, 0.5) // to the left of triangle
        }.asFloatArray()

        val polygonType = PolygonTypeDetector().determinePolygonType(polygonVertices)

        Assertions.assertThat(polygonType).isEqualTo(PolygonType.CONCAVE)
    }
}
