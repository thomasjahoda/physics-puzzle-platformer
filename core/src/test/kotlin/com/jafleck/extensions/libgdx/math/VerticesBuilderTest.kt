package com.jafleck.extensions.libgdx.math

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class VerticesBuilderTest {

    @Test
    fun `1 vertex as float array`() {
        val polygonVertices = buildVertices {
            p(0.5f, 1f)
        }.asFloatArray()

        Assertions.assertThat(polygonVertices)
            .isEqualTo(floatArrayOf(0.5f, 1f))
    }

    @Test
    fun `vertices as float array`() {
        val polygonVertices = buildVertices {
            p(1, 2)
            p(3f, 4f)
            p(5, 6)
            p(7.1, 8.2)
        }.asFloatArray()

        Assertions.assertThat(polygonVertices)
            .isEqualTo(floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7.1f, 8.2f))
    }

}
