package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.game.components.entities.DeathZoneComponent
import com.jafleck.game.entities.DeathZoneEntity
import com.jafleck.game.util.ashley.getDebugDump
import com.jafleck.game.util.logger
import ktx.ashley.allOf


class DeathZoneSystem : IteratingSystem(allOf(DeathZoneComponent::class).get()) {

    private val logger = logger(this::class)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val typedEntity = DeathZoneEntity(entity)
        val entitiesWithinZone = typedEntity.entityCollisionTrackingZone.entitiesWithinZone
        if (entitiesWithinZone.isNotEmpty()) {
            entitiesWithinZone.forEach {
                logger.debug { "Entity ${entity.getDebugDump()} is in death zone, removing it" }
                engine.removeEntity(it)
            }
        }
    }
}
