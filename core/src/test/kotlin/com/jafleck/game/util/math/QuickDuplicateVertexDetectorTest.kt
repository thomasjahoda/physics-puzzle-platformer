package com.jafleck.game.util.math

import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.math.buildVertices
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class QuickDuplicateVertexDetectorTest {

    @Test
    fun `no duplicate - 1`() {
        val vertices = buildVertices {
            p(0, 0)
            p(1, 1)
            p(2, 2)
        }.asFloatArray()

        Assertions.assertThat(QuickDuplicateVertexDetector.getFirstDuplicate(vertices))
            .isEqualTo(null)
    }

    @Test
    fun `no duplicate - 2`() {
        val vertices = buildVertices {
            p(0, 0)
            p(0, 1)
            p(0, 2)
        }.asFloatArray()

        Assertions.assertThat(QuickDuplicateVertexDetector.getFirstDuplicate(vertices))
            .isEqualTo(null)
    }

    @Test
    fun `no duplicate - 3`() {
        val vertices = buildVertices {
            p(0, 0)
            p(1, 0)
            p(2, 0)
        }.asFloatArray()

        Assertions.assertThat(QuickDuplicateVertexDetector.getFirstDuplicate(vertices))
            .isEqualTo(null)
    }

    @Test
    fun `duplicate - 1`() {
        val vertices = buildVertices {
            p(0, 0)
            p(1, 0)
            p(2, 0)
            p(0, 0)
        }.asFloatArray()

        Assertions.assertThat(QuickDuplicateVertexDetector.getFirstDuplicate(vertices))
            .isEqualTo(Vector2(0f, 0f))
    }

    @Test
    fun `duplicate - 2`() {
        val vertices = buildVertices {
            p(0, 0)
            p(0, 0)
            p(1, 0)
            p(2, 0)
        }.asFloatArray()

        Assertions.assertThat(QuickDuplicateVertexDetector.getFirstDuplicate(vertices))
            .isEqualTo(Vector2(0f, 0f))
    }

    @Test
    fun `duplicate - 3`() {
        val vertices = buildVertices {
            p(15, 25)
            p(50, 25) // duplicate
            p(51, 23)
            p(15, 26)
            p(13, 11)
            p(50, 25) // duplicate
        }.asFloatArray()

        Assertions.assertThat(QuickDuplicateVertexDetector.getFirstDuplicate(vertices))
            .isEqualTo(Vector2(50f, 25f))
    }

    @Test
    fun `only duplicates`() {
        val vertices = buildVertices {
            p(50, 25)
            p(50, 25)
            p(50, 25)
            p(50, 25)
            p(50, 25)
        }.asFloatArray()

        Assertions.assertThat(QuickDuplicateVertexDetector.getFirstDuplicate(vertices))
            .isEqualTo(Vector2(50f, 25f))
    }
}
