package com.jafleck.extensions.libgdx.rendering

import com.jafleck.extensions.kotlin.round
import com.jafleck.extensions.libgdx.math.buildVertices
import com.jafleck.extensions.libgdx.math.toListOfVertices
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class PolygonCircleTest {


    @Test
    fun `simple - radius of 1 and 4 segments`() {
        val polygonVertices = createCirclePolygon(1f, 4)

        Assertions.assertThat(polygonVertices.round(3).toListOfVertices())
            .isEqualTo(buildVertices {
                p(0f, 1f)
                p(-1f, -0f)
                p(0f, -1f)
                p(1f, 0f)
            }.asListOfVertices())
    }
}
