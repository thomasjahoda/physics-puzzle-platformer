package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.entities.StickyRopePartComponent
import com.jafleck.game.entities.RopeEntityCreator
import com.jafleck.game.entities.RopePartEntity
import com.jafleck.game.entities.ThrownRopeEntity
import com.jafleck.game.util.box2d.ContactListenerMultiplexer
import com.jafleck.game.util.libgdx.box2d.entity
import com.jafleck.game.util.libgdx.box2d.getEntityWithFixtureByComponentOrNull
import ktx.box2d.revoluteJointWith
import ktx.math.minus


class ThrownRopeSystem(
    private val ropeEntityCreator: RopeEntityCreator,
    private val contactListenerMultiplexer: ContactListenerMultiplexer
) : IteratingSystem(ThrownRopeEntity.family), ContactListener {

    private val stickyRopePartsToAttach = mutableListOf<Triple<Fixture, Fixture, Vector2>>()

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        contactListenerMultiplexer.addListener(this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        contactListenerMultiplexer.removeListener(this)
    }

    override fun update(deltaTime: Float) {
        processStickyRopePartCollisions()
        super.update(deltaTime)
    }

    private fun processStickyRopePartCollisions() {
        stickyRopePartsToAttach.forEach {
            val (collidedRopePartFixture, fixtureToAttachTo, attachmentPoint) = it
            val ropePartEntity = RopePartEntity(collidedRopePartFixture.body.entity)
            val stickyRopePartComponent = ropePartEntity.entity[StickyRopePartComponent]
            if (stickyRopePartComponent.anchored) return

            stickyRopePartComponent.anchoredBy = collidedRopePartFixture.body.revoluteJointWith(fixtureToAttachTo.body) {
                initialize(bodyA, bodyB, attachmentPoint)
            }
            stickyRopePartComponent.anchoredTo = fixtureToAttachTo.body.entity

            // anchor rope to thrower if it is thrown rope
            val ropeEntity = ropePartEntity.ropePart.owningRopeEntity
            val thrownRopeEntity = ropeEntity.asThrownRope()
            if (thrownRopeEntity != null) {
                if (!thrownRopeEntity.thrownRope.anchored) {
                    anchorThrownRope(thrownRopeEntity)
                }
            }
        }
        stickyRopePartsToAttach.clear()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (deltaTime == 0f) return

        val thrownRope = ThrownRopeEntity(entity)
        if (!thrownRope.thrownRope.anchored) {
            addNewPartIfSpaceIsFree(thrownRope)
        }
    }

    private fun addNewPartIfSpaceIsFree(thrownRope: ThrownRopeEntity) {
        val thrower = thrownRope.thrownRope.thrownBy
        val firstRopePart = RopePartEntity(thrownRope.rope.parts[0])
        val worldAnchorPointToThrower = thrower.body.value.getWorldPoint(thrownRope.thrownRope.throwerLocalAnchorPoint)
        val distanceVectorFromFirstPartToOriginatingPosition = firstRopePart.asPhysicalShapedEntity().position.vector - worldAnchorPointToThrower
        if (distanceVectorFromFirstPartToOriginatingPosition.len() > RopePartEntity.ROPE_PART_SIZE.y * 1.5f) {
            ropeEntityCreator.createNormalRopePartAttachedTo(worldAnchorPointToThrower, firstRopePart)
            addNewPartIfSpaceIsFree(thrownRope)
        }
    }

    override fun beginContact(contact: Contact) {
        withItIfNotNull(contact.getEntityWithFixtureByComponentOrNull(StickyRopePartComponent)) {
            val (entity, fixture, otherFixture, isFixtureA) = it
            val stickyRopePartComponent = entity[StickyRopePartComponent]
            if (!stickyRopePartComponent.anchored) {
                val worldManifold = contact.worldManifold
                if (worldManifold.numberOfContactPoints != 0) {
                    val contactPoint = worldManifold.points[0] // TODO improve, use middle point in case there are two contact points (poly on poly)
                    stickyRopePartsToAttach.add(Triple(fixture, otherFixture, contactPoint))
                }
            }
        }
    }

    private fun anchorThrownRope(thrownRopeEntity: ThrownRopeEntity) {
        require(thrownRopeEntity.thrownRope.anchored.not())
        val throwerEntity = thrownRopeEntity.thrownRope.thrownBy
        val firstRopePartEntityOfRope = RopePartEntity(thrownRopeEntity.rope.parts[0])
        thrownRopeEntity.thrownRope.anchoredBy = throwerEntity.body.value.revoluteJointWith(firstRopePartEntityOfRope.asPhysicalShapedEntity().body.value) {
            initialize(bodyA, bodyB, throwerEntity.body.value.getWorldPoint(thrownRopeEntity.thrownRope.throwerLocalAnchorPoint))
        }
    }

    override fun endContact(contact: Contact) {

    }

    override fun preSolve(contact: Contact, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse?) {
    }
}