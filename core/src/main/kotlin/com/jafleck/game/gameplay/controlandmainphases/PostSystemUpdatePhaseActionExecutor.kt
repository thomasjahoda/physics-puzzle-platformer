package com.jafleck.game.gameplay.controlandmainphases

import com.jafleck.game.util.logger

/**
 * Executes actions after the current Ashley system update.
 */
class PostSystemUpdatePhaseActionExecutor {

    private val logger = logger(this::class)
    private val actions: MutableList<PhaseAction> = mutableListOf()

    fun update() {
        if (actions.isNotEmpty()) {
            logger.debug { "Executing ${actions.size} actions at the end of the tick (now)" }
            actions.forEach {
                it()
            }
            actions.clear()
        }
    }

    fun executeActionAfterTheCurrentTick(phaseAction: PhaseAction) {
        actions.add(phaseAction)
    }
}
