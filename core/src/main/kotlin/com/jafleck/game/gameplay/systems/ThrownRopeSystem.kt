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
import com.jafleck.game.util.logger
import ktx.box2d.revoluteJointWith
import ktx.math.minus


class ThrownRopeSystem(
    private val ropeEntityCreator: RopeEntityCreator,
    private val contactListenerMultiplexer: ContactListenerMultiplexer
) : IteratingSystem(ThrownRopeEntity.family), ContactListener {

    private val stickyRopePartsToAttach = mutableListOf<Triple<Fixture, Fixture, Vector2>>()

    private val logger = logger(this::class)

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        contactListenerMultiplexer.addListener(this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        contactListenerMultiplexer.removeListener(this)
    }

    override fun update(deltaTime: Float) {
        if (deltaTime == 0f) return

        processStickyRopePartCollisions()
        super.update(deltaTime)
    }

    private fun processStickyRopePartCollisions() {
        stickyRopePartsToAttach.forEach {
            val (collidedRopePartFixture, fixtureToAttachTo, attachmentPoint) = it
            logger.debug { "Processing event that sticky rope part should be attached to a collided fixture. ($it)" }
            val ropePartEntity = RopePartEntity(collidedRopePartFixture.body.entity)
            val stickyRopePartComponent = ropePartEntity.entity[StickyRopePartComponent]
            if (!stickyRopePartComponent.anchored) {
                stickyRopePartComponent.anchoredBy = collidedRopePartFixture.body.revoluteJointWith(fixtureToAttachTo.body) {
                    initialize(bodyA, bodyB, attachmentPoint)
                }
                stickyRopePartComponent.anchoredTo = fixtureToAttachTo.body.entity

                // anchor rope to thrower if it is thrown rope
                val ropeEntity = ropePartEntity.ropePart.owningRopeEntity
                val thrownRopeEntity = ropeEntity.asThrownRope()
                if (thrownRopeEntity != null) {
                    if (!thrownRopeEntity.thrownRope.anchored) {
                        anchorThrownRopeToThrower(thrownRopeEntity)
                    }
                }
            }
        }
        stickyRopePartsToAttach.clear()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (deltaTime == 0f) return

        val thrownRope = ThrownRopeEntity(entity)
        if (!thrownRope.thrownRope.anchored) {
            // note: the maxLengthInParts may be overstepped if the player moves fast and multiple parts have been added at one time
            addNewPartsIfSpaceIsFree(thrownRope)
            if (thrownRope.rope.parts.size >= thrownRope.thrownRope.maxLengthInParts) {
                anchorThrownRopeToThrower(thrownRope)
            }
        }
    }

    private fun addNewPartsIfSpaceIsFree(thrownRope: ThrownRopeEntity) {
        val thrower = thrownRope.thrownRope.thrownBy
        val firstRopePart = RopePartEntity(thrownRope.rope.parts[0])
        val worldAnchorPointToThrower = thrower.body.value.getWorldPoint(thrownRope.thrownRope.throwerLocalAnchorPoint)
        val distanceVectorFromFirstPartToOriginatingPosition = firstRopePart.asPhysicalShapedEntity().position.vector - worldAnchorPointToThrower
        if (distanceVectorFromFirstPartToOriginatingPosition.len() > RopePartEntity.ROPE_PART_SIZE.y * 1.5f) {
            val velocity = thrower.velocity!!.vector.cpy()
            ropeEntityCreator.createNormalRopePartAttachedTo(worldAnchorPointToThrower, firstRopePart, velocity)
            addNewPartsIfSpaceIsFree(thrownRope)
        }
    }

    override fun beginContact(contact: Contact) {
        withItIfNotNull(contact.getEntityWithFixtureByComponentOrNull(StickyRopePartComponent)) {
            val (entity, fixture, otherFixture, _) = it
            val stickyRopePartComponent = entity[StickyRopePartComponent]
            if (!stickyRopePartComponent.anchored) {
                val worldManifold = contact.worldManifold
                if (worldManifold.numberOfContactPoints != 0) {
                    val contactPoint = worldManifold.points[0] // TODO improve, use middle point in case there are two contact points (poly on poly)
                    // attach sticky rope part to fixture asynchronously (outside of physics simulation)
                    val element = Triple(fixture, otherFixture, contactPoint)
                    logger.debug { "A sticky rope part has collided with an fixture and should be anchored after the simulation. Adding $element to list of events to process." }
                    stickyRopePartsToAttach.add(element)
                }
            }
        }
    }

    private fun anchorThrownRopeToThrower(thrownRopeEntity: ThrownRopeEntity) {
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
