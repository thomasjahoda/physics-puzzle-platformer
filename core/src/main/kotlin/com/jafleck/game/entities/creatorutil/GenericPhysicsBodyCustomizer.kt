package com.jafleck.game.entities.creatorutil

import com.badlogic.gdx.physics.box2d.Body
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.game.entities.customizations.GenericEntityCustomization
import ktx.box2d.FixtureDefinition

class GenericPhysicsBodyCustomizer {

    /**
     * Customizes physics properties of fixture and the resulting body .
     */
    fun customizePhysicsProperties(customization: GenericEntityCustomization,
                                   fixtureDefinition: FixtureDefinition) {
        withItIfNotNull(customization.density) {
            fixtureDefinition.density = it
        }
        withItIfNotNull(customization.friction) {
            fixtureDefinition.friction = it
        }
        withItIfNotNull(customization.restitution) {
            fixtureDefinition.restitution = it
        }

        customizeBodyPropertiesWhenBodyIsReady(customization, fixtureDefinition)

    }

    private fun customizeBodyPropertiesWhenBodyIsReady(customization: GenericEntityCustomization,
                                                       fixtureDefinition: FixtureDefinition) {
        if (fixtureDefinition.creationCallback != null) error("would overwrite creationCallback")
        fixtureDefinition.creationCallback = { fixture ->
            customizePhysicsPropertiesOfBody(customization, fixture.body)
        }
    }

    private fun customizePhysicsPropertiesOfBody(customization: GenericEntityCustomization, body: Body) {
        withItIfNotNull(customization.linearDamping) {
            body.linearDamping = it
        }
        withItIfNotNull(customization.angularDamping) {
            body.angularDamping = it
        }
        withItIfNotNull(customization.gravityScale) {
            body.gravityScale = it
        }
        withItIfNotNull(customization.fixedRotation) {
            body.isFixedRotation = it
        }
    }
}

fun FixtureDefinition.apply(customization: GenericEntityCustomization,
                            genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer): FixtureDefinition {
    genericPhysicsBodyCustomizer.customizePhysicsProperties(customization, this)
    return this
}
