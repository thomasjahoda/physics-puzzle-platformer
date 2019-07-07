package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.RemoveAfterDurationComponent
import ktx.ashley.allOf


class RemoveEntityAfterDurationSystem(
    priority: Int
) : IteratingSystem(allOf(
    RemoveAfterDurationComponent::class
).get(), priority) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val component = entity[RemoveAfterDurationComponent]
        component.secondsLeft -= deltaTime
        if (component.secondsLeft <= 0) {
            engine.removeEntity(entity)
        }
    }
}