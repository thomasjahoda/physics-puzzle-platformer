package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.physics.OthersCanPassThroughFromBelowComponent
import com.jafleck.game.components.physics.PassesThroughFromBelowComponent
import com.jafleck.game.util.box2d.ContactListenerMultiplexer
import com.jafleck.game.util.libgdx.box2d.entity
import com.jafleck.game.util.libgdx.box2d.processIfComponentInvolved
import com.jafleck.game.util.libgdx.box2d.processIfComponentsCollide
import com.jafleck.game.util.logger
import ktx.ashley.remove


class PassThroughFromBelowSystem(
    private val contactListenerMultiplexer: ContactListenerMultiplexer
) : EntitySystem(), ContactListener {

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

    override fun beginContact(contact: Contact) {
        contact.processIfComponentInvolved(OthersCanPassThroughFromBelowComponent) { ownerEntity, _, ownerFixture, otherFixture ->
            val otherBody = otherFixture.body
            val otherEntity = otherBody.entity
            logger.debug { "$ownerEntity has started to collide with $otherEntity -> determining whether it can pass through" }
            withItIfNotNull(otherEntity.getOrNull(PassesThroughFromBelowComponent)) {
                it.passingThroughCount++
                return
            }

            val otherBodyPassThroughAngleDegrees = otherBody.linearVelocity.cpy().nor().angle()
            val ownerRotationDegrees = ownerEntity.getOrNull(RotationComponent)?.degrees ?: 0f
            var passThroughAngleDegreesMin = ownerRotationDegrees - 0f
            var passThroughAngleDegreesMax = ownerRotationDegrees + 180f
            if (passThroughAngleDegreesMax > 360f) {
                val correction = passThroughAngleDegreesMax.rem(360f) - passThroughAngleDegreesMax
                passThroughAngleDegreesMax += correction
                passThroughAngleDegreesMin += correction
            }
            if (otherBodyPassThroughAngleDegrees in passThroughAngleDegreesMin..passThroughAngleDegreesMax) {
                logger.debug { "- may pass through" }
                otherEntity.add(PassesThroughFromBelowComponent())
            } else {
                logger.debug { "- may not pass through! other degree: $otherBodyPassThroughAngleDegrees, min allowed: $passThroughAngleDegreesMin, max allowed: $passThroughAngleDegreesMax" }
            }
        }
    }


    override fun endContact(contact: Contact) {
        contact.processIfComponentsCollide(OthersCanPassThroughFromBelowComponent, PassesThroughFromBelowComponent)
        { _, _, _, entityB, componentB, _ ->
            if (componentB.passingThroughCount == 1) {
                entityB.remove<PassesThroughFromBelowComponent>()
            } else {
                componentB.passingThroughCount--
            }
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold?) {
        contact.processIfComponentsCollide(OthersCanPassThroughFromBelowComponent, PassesThroughFromBelowComponent)
        { _, _, _, _, _, _ ->
            contact.isEnabled = false
        }
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse?) {
    }
}
