package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.has
import com.jafleck.game.components.basic.BodyComponent
import com.jafleck.game.components.entities.WaterComponent
import com.jafleck.game.components.logic.PushedUpByWaterComponent
import com.jafleck.game.entities.WaterEntity
import com.jafleck.game.families.ShapedEntity
import com.jafleck.game.util.box2d.ContactListenerMultiplexer
import com.jafleck.game.util.libgdx.box2d.entity
import com.jafleck.game.util.libgdx.box2d.processIfComponentInvolved
import com.jafleck.game.util.logger
import ktx.ashley.allOf
import ktx.ashley.remove
import kotlin.math.min


class WaterSystem(
    private val contactListenerMultiplexer: ContactListenerMultiplexer
) : IteratingSystem(allOf(PushedUpByWaterComponent::class).get()), ContactListener {
    // refactor to use EntityCollisionTrackingZoneSystem?

    private val logger = logger(this::class)

    companion object {
        const val WATER_DENSITY = 1f
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val water = entity[PushedUpByWaterComponent].waterEntity

        val shapedEntity = ShapedEntity(entity)
        val rectangle = shapedEntity.shape.getRectangleAroundShape(Vector2())
        val body = entity[BodyComponent].value
        val waterTopY = water.position.originY + water.asShapedEntity().shape.getRectangleAroundShape(Vector2()).y / 2
        val submergedEntityTopY = shapedEntity.position.originY + rectangle.y / 2
        val submergedDistance = rectangle.y - (submergedEntityTopY - waterTopY)
        val submergedPercentage = min(1f, submergedDistance / rectangle.y)

        val bodyDensity = body.fixtureList[0].density // multiple fixtures with different mass not supported currently
        val waterArea = (body.mass / bodyDensity) * submergedPercentage

        val impulse = Vector2(0f, waterArea * WATER_DENSITY)
        body.applyLinearImpulse(impulse, body.position, true)
        body.linearDamping = (body.linearVelocity.len() * body.linearVelocity.len()) / 5
//        logger.debug { "impulse: $impulse" }
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        contactListenerMultiplexer.addListener(this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        contactListenerMultiplexer.removeListener(this)
    }

    override fun beginContact(contact: Contact) {
        contact.processIfComponentInvolved(WaterComponent) { waterEntity, _, _, submergedFixture ->
            logger.debug { "Begin contact with water" }

            val submergedBody = submergedFixture.body
            val submergedEntity = submergedBody.entity
            if (submergedEntity.has(PushedUpByWaterComponent)) {
                logger.debug { "Body seems to have multiple fixtures because it is already pushed up by water" }
            } else {
                logger.debug { submergedBody.linearDamping.toString() }
                submergedEntity.add(PushedUpByWaterComponent(WaterEntity(waterEntity), submergedBody.linearDamping))
                submergedBody.linearDamping = 1f
            }
        }
    }

    override fun endContact(contact: Contact) {
        contact.processIfComponentInvolved(WaterComponent) { _, _, _, submergedFixture ->
            logger.debug { "End contact with water" }
            val submergedBody = submergedFixture.body
            val pushedUpByWaterComponent = submergedBody.entity.remove<PushedUpByWaterComponent>() as PushedUpByWaterComponent?
            if (pushedUpByWaterComponent != null) {
                submergedBody.linearDamping = pushedUpByWaterComponent.originalLinearDamping
            } else {
                logger.debug { "No PushedUpByWaterComponent was found on the entity. This probably happened because the body consists of multiple fixtures and therefore generates multiple contacts." }
            }
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse?) {
    }
}
