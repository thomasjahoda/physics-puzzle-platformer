package com.jafleck.game.gameplay.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.jafleck.extensions.libgdx.scenes.scene2d.percentWidthOfTable
import com.jafleck.game.gameplay.systems.debug.CursorDebugSystem
import ktx.scene2d.KContainer
import ktx.scene2d.table

class DebugRow(
    private val cursorDebugSystem: CursorDebugSystem?,
    private val manualTimeControl: ManualTimeControl?,
    private val fpsCounter: FpsCounter?
) {


    val content: Actor = {
        table {
            if (cursorDebugSystem != null) {
                add(cursorDebugSystem.worldCoordsOfCursorLabel).apply {
                    percentWidthOfTable(0.6f)
                }
            }
            if (fpsCounter != null) {
                add(fpsCounter.fpsLabel).apply {
                    percentWidthOfTable(0.3f)
                }
            }
            add(KContainer<Actor>()).expandX().fillX()
        }
    }()
}
