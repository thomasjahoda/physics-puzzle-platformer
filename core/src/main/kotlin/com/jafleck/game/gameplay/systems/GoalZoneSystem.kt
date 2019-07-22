package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.entities.GoalZoneComponent
import com.jafleck.game.components.zone.EntityCollisionTrackingZoneComponent
import com.jafleck.game.util.logger
import ktx.ashley.allOf


class GoalZoneSystem : IteratingSystem(allOf(GoalZoneComponent::class).get()) {

    private val logger = logger(this::class)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (entity[EntityCollisionTrackingZoneComponent].entitiesWithinZone.isNotEmpty()) {
            logger.debug { "GOOOOOOOOOOAAAAAAAAAAAAAAAAALLLLLLLL, GOOOOOOOOOOAAAAAAAALLLLLLLLLL!!!" }
        }
    }
}
