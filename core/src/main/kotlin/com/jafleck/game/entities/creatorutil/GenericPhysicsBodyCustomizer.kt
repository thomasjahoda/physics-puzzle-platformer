package com.jafleck.game.entities.creatorutil

import com.badlogic.gdx.physics.box2d.Body
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.game.entities.config.GenericEntityConfig
import ktx.box2d.FixtureDefinition

class GenericPhysicsBodyCustomizer {

    /**
     * Customizes physics properties of fixture and the resulting body .
     */
    fun customizePhysicsProperties(config: GenericEntityConfig,
                                   fixtureDefinition: FixtureDefinition) {
        withItIfNotNull(config.density) {
            fixtureDefinition.density = it
        }
        withItIfNotNull(config.friction) {
            fixtureDefinition.friction = it
        }
        withItIfNotNull(config.restitution) {
            fixtureDefinition.restitution = it
        }

        customizeBodyPropertiesWhenBodyIsReady(config, fixtureDefinition)

    }

    private fun customizeBodyPropertiesWhenBodyIsReady(config: GenericEntityConfig,
                                                       fixtureDefinition: FixtureDefinition) {
        if (fixtureDefinition.creationCallback != null) error("would overwrite creationCallback")
        fixtureDefinition.creationCallback = { fixture ->
            customizePhysicsPropertiesOfBody(config, fixture.body)
        }
    }

    private fun customizePhysicsPropertiesOfBody(config: GenericEntityConfig, body: Body) {
        withItIfNotNull(config.linearDamping) {
            body.linearDamping = it
        }
        withItIfNotNull(config.angularDamping) {
            body.angularDamping = it
        }
        withItIfNotNull(config.gravityScale) {
            body.gravityScale = it
        }
        withItIfNotNull(config.fixedRotation) {
            body.isFixedRotation = it
        }
    }
}

fun FixtureDefinition.apply(config: GenericEntityConfig,
                            genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer): FixtureDefinition {
    genericPhysicsBodyCustomizer.customizePhysicsProperties(config, this)
    return this
}
