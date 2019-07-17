package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.logic.RemoveAfterDurationComponent
import com.jafleck.game.util.logger
import ktx.ashley.allOf


class RemoveEntityAfterDurationSystem : IteratingSystem(allOf(
    RemoveAfterDurationComponent::class
).get()) {

    private val logger = logger(this::class)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val component = entity[RemoveAfterDurationComponent]
        component.secondsLeft -= deltaTime
        if (component.secondsLeft <= 0) {
            logger.debug { "Removing $entity because its ${RemoveAfterDurationComponent::class.simpleName} has run out of time." }
            engine.removeEntity(entity)
        }
    }
}
