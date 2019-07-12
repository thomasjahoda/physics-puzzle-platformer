package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.BodyComponent
import com.jafleck.game.components.PushedUpByWaterComponent
import com.jafleck.game.entities.WaterEntity
import com.jafleck.game.families.ShapedEntity
import com.jafleck.game.util.libgdx.box2d.entity
import com.jafleck.game.util.logger
import ktx.ashley.allOf
import ktx.ashley.remove
import ktx.math.times
import kotlin.math.max
import kotlin.math.min


class WaterSystem(
    private val world: World
) : IteratingSystem(allOf(PushedUpByWaterComponent::class).get()), ContactListener {
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

        val waterArea = (body.mass / body.fixtureList[0].density) * submergedPercentage

        val impulse = Vector2(0f, waterArea * WATER_DENSITY)
        body.applyLinearImpulse(impulse, body.position, true)
        body.linearDamping = (body.linearVelocity.len() * body.linearVelocity.len()) / 5
        logger.debug { "impulse: $impulse" }
    }

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        world.setContactListener(this)
    }

    override fun beginContact(contact: Contact) {
        if (contact.fixtureA.isSensor or contact.fixtureB.isSensor) {
            logger.debug { "Begin contact with water" }
            val waterFixture = if (contact.fixtureA.isSensor) contact.fixtureA else contact.fixtureB
            val otherFixture = if (contact.fixtureA.isSensor) contact.fixtureB else contact.fixtureA

            val body = otherFixture.body
            logger.debug { body.linearDamping.toString() }
            body.entity.add(PushedUpByWaterComponent(WaterEntity(waterFixture.body.entity), body.linearDamping))
            body.linearDamping = 1f
        }
    }

    override fun endContact(contact: Contact) {
        if (contact.fixtureA.isSensor or contact.fixtureB.isSensor) {
            logger.debug { "End contact with water" }
            val otherFixture = if (contact.fixtureA.isSensor) contact.fixtureB else contact.fixtureA
            val body = otherFixture.body
            val pushedUpByWaterComponent = body.entity.remove<PushedUpByWaterComponent>() as PushedUpByWaterComponent
            body.linearDamping = pushedUpByWaterComponent.originalLinearDamping
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse?) {
    }
}
