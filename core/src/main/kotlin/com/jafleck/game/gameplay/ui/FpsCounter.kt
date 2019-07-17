package com.jafleck.game.gameplay.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.jafleck.extensions.kotlin.round
import ktx.scene2d.Scene2DSkin

class FpsCounter {
    companion object {
        internal const val RELEVANT_FRAME_COUNT = 20
    }

    private val tickTimes = FloatArray(RELEVANT_FRAME_COUNT)
    private var nextTickTimesIndex = 0
    private var totalFrameDurationSeconds = 0f

    val fpsLabel = Label("initial text", Scene2DSkin.defaultSkin)

    fun calculateFps(deltaSeconds: Float): Float {
        val removedTickTime = tickTimes[nextTickTimesIndex]
        totalFrameDurationSeconds -= removedTickTime

        totalFrameDurationSeconds += deltaSeconds
        tickTimes[nextTickTimesIndex] = deltaSeconds

        if (++nextTickTimesIndex == RELEVANT_FRAME_COUNT) {
            nextTickTimesIndex = 0
        }

        val fps = RELEVANT_FRAME_COUNT / totalFrameDurationSeconds
        fpsLabel.setText("FPS: ${fps.round(2)}")
        return fps
    }
}
