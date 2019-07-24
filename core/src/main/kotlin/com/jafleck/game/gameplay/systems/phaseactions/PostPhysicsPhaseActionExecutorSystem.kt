package com.jafleck.game.gameplay.systems.phaseactions

import com.badlogic.ashley.core.EntitySystem
import com.jafleck.game.gameplay.controlandmainphases.PhaseAction
import com.jafleck.game.util.logger

class PostPhysicsPhaseActionExecutorSystem : EntitySystem() {

    private val logger = logger(this::class)
    private val actions: MutableList<PhaseAction> = mutableListOf()

    override fun update(deltaTime: Float) {
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
