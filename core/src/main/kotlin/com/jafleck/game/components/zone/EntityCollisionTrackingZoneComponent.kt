package com.jafleck.game.components.zone

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Fixture
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class EntityCollisionTrackingZoneComponent(
    val entitiesWithinZone: MutableSet<Entity> = mutableSetOf(),
    /**
     * This needs to be tracked because there might be multiple fixtures of one body in contact with another entity.
     */
    val fixtureCollisionsWithinZoneByEntity: MutableMap<Entity, MutableSet<FixtureInZoneCollision>> = mutableMapOf()
) : Component {

    companion object : ComponentMapperAccessor<EntityCollisionTrackingZoneComponent>(EntityCollisionTrackingZoneComponent::class)
}

data class FixtureInZoneCollision(val fixtureInZone: Fixture, val zoneFixture: Fixture)
