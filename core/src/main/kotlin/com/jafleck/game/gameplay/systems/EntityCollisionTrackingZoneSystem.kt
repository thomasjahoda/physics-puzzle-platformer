package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.jafleck.game.components.zone.EntityCollisionTrackingZoneComponent
import com.jafleck.game.components.zone.FixtureInZoneCollision
import com.jafleck.game.util.box2d.ContactListenerMultiplexer
import com.jafleck.game.util.libgdx.box2d.entity
import com.jafleck.game.util.libgdx.box2d.processIfComponentInvolved
import com.jafleck.game.util.logger
import kotlin.collections.set


class EntityCollisionTrackingZoneSystem(
    private val contactListenerMultiplexer: ContactListenerMultiplexer
) : EntitySystem(), ContactListener {

    private val logger = logger(this::class)

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        contactListenerMultiplexer.addListener(this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        contactListenerMultiplexer.removeListener(this)
    }

    override fun beginContact(contact: Contact) {
        // TODO there might be cleanup issues here when a body is removed while it is within the zone.
        //  To prevent this, an entity family listener should be set up to remove the removed entity from any currently colliding entity.
        //  Another map must be set up to track this entity collision status.
        //  Either a global map or an Component added to the other entity, something like CollidingWithEntityTrackingZoneComponent (which could be poolable)
        contact.processIfComponentInvolved(EntityCollisionTrackingZoneComponent) { _, relevantComponent, ownerFixture, otherFixture ->
            val otherEntity = otherFixture.body.entity
            val fixturesWithinZone = relevantComponent.fixtureCollisionsWithinZoneByEntity[otherEntity]
            if (fixturesWithinZone == null) {
                relevantComponent.entitiesWithinZone.add(otherEntity)
                relevantComponent.fixtureCollisionsWithinZoneByEntity[otherEntity] = mutableSetOf(FixtureInZoneCollision(otherFixture, ownerFixture))
            } else {
                fixturesWithinZone.add(FixtureInZoneCollision(otherFixture, ownerFixture))
            }
        }
    }

    override fun endContact(contact: Contact) {
        contact.processIfComponentInvolved(EntityCollisionTrackingZoneComponent) { _, relevantComponent, ownerFixture, otherFixture ->
            val otherEntity = otherFixture.body.entity
            val fixturesWithinZone = relevantComponent.fixtureCollisionsWithinZoneByEntity[otherEntity]
                ?: error("This should never happen")
            if (fixturesWithinZone.size == 1) {
                relevantComponent.entitiesWithinZone.remove(otherEntity)
                relevantComponent.fixtureCollisionsWithinZoneByEntity.remove(otherEntity)
            } else {
                fixturesWithinZone.remove(FixtureInZoneCollision(otherFixture, ownerFixture))
            }
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse?) {
    }
}
