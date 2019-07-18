package com.jafleck.game.gameplay.ui

import com.jafleck.testutil.HeadlessLibgdxAndMockedGraphics
import org.assertj.core.api.Assertions
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(HeadlessLibgdxAndMockedGraphics::class)
internal class FpsCounterTest {

    @Test
    fun `same frame-delta results in same frame-rate`() {
        val fpsCounter = FpsCounter()
        var fps = 0f

        for (i in 0..300) {
            fps = fpsCounter.calculateFps(1 / 60f)
        }

        assertFps(fps, 60f)
    }

    @Test
    fun `2 different times result in something between the two`() {
        val fpsCounter = FpsCounter()
        var fps = 0f

        for (i in 0 until FpsCounter.RELEVANT_FRAME_COUNT / 2) {
            fps = fpsCounter.calculateFps(1 / 60f)
        }
        for (i in FpsCounter.RELEVANT_FRAME_COUNT / 2 until FpsCounter.RELEVANT_FRAME_COUNT) {
            fps = fpsCounter.calculateFps(1 / 30f)
        }

        assertFps(fps, 40f)
    }

    private fun assertFps(actual: Float, expected: Float) {
        Assertions.assertThat(actual).isCloseTo(expected, Offset.offset(0.01f))
    }
}
