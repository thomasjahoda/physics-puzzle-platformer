package com.jafleck.game.gameplay.ui

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.jafleck.game.util.input.UiInputMultiplexer

class ManualTimeControl(
    uiInputMultiplexer: UiInputMultiplexer
) {
    companion object {
        const val FRAME_DURATION = 1 / 60f
    }

    init {
        uiInputMultiplexer.addProcessor(InputListener())
    }

    private var manualMode = false
    private var advanceFrames: Int = 0

    inner class InputListener : InputAdapter() {

        override fun keyDown(keycode: Int): Boolean {
            if (keycode == Input.Keys.M) {
                manualMode = !manualMode
                advanceFrames = 0
                return true
            }
            if (keycode == Input.Keys.PLUS) {
                if (manualMode) {
                    advanceFrames++
                    return true
                }
            }
            return false
        }
    }

    fun transformDeltaTime(deltaSeconds: Float): Float {
        if (manualMode) {
            if (advanceFrames != 0) {
                advanceFrames--
                return FRAME_DURATION
            } else {
                return 0f
            }
        } else {
            return deltaSeconds
        }
    }
}
