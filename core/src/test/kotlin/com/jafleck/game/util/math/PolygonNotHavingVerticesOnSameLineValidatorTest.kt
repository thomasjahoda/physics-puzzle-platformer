package com.jafleck.game.util.math

import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.math.buildVertices
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PolygonNotHavingVerticesOnSameLineValidatorTest {

    @Test
    fun `ok - 1`() {
        val vertices = buildVertices {
            p(0, 0)
            p(1, 1)
            p(1, 0)
        }.asFloatArray()

        Assertions.assertThat(PolygonNotHavingVerticesOnSameLineValidator.getFirstVertexOnLineAsOther(vertices))
            .isEqualTo(null)
    }
    @Test
    fun `ok - 2`() {
        val vertices = buildVertices {
            p(0, 0)
            p(1, 1)
            p(1, 0)
            p(0.5, -0.5)
        }.asFloatArray()

        Assertions.assertThat(PolygonNotHavingVerticesOnSameLineValidator.getFirstVertexOnLineAsOther(vertices))
            .isEqualTo(null)
    }
    @Test
    fun `nok - 1`() {
        val vertices = buildVertices {
            p(0, 0)
            p(1, 1)
            p(2, 2)
        }.asFloatArray()

        Assertions.assertThat(PolygonNotHavingVerticesOnSameLineValidator.getFirstVertexOnLineAsOther(vertices))
            .isEqualTo(Vector2(0f, 0f))
    }
}
