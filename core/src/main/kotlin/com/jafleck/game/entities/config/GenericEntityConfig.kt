package com.jafleck.game.entities.config

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.kotlin.withItIfNotNull

data class GenericEntityConfig(
    var initialVelocity: Vector2? = null,
    var density: Float? = null,
    var friction: Float? = null,
    var restitution: Float? = null,
    var linearDamping: Float? = null,
    var angularDamping: Float? = null,
    var gravityScale: Float? = null,
    var fixedRotation: Boolean? = null,
    var fillColor: Color? = null,
    var borderColor: Color? = null,
    var borderThickness: Float? = null,
    var visualLayerIndex: Int? = null
) {
    fun combine(other: GenericEntityConfig): GenericEntityConfig {
        return copy().apply(other)
    }

    fun apply(other: GenericEntityConfig): GenericEntityConfig {
        withItIfNotNull(other.initialVelocity) { initialVelocity = it }
        withItIfNotNull(other.density) { density = it }
        withItIfNotNull(other.friction) { friction = it }
        withItIfNotNull(other.restitution) { restitution = it }
        withItIfNotNull(other.linearDamping) { linearDamping = it }
        withItIfNotNull(other.angularDamping) { angularDamping = it }
        withItIfNotNull(other.gravityScale) { gravityScale = it }
        withItIfNotNull(other.fixedRotation) { fixedRotation = it }
        withItIfNotNull(other.fillColor) { fillColor = it }
        withItIfNotNull(other.borderColor) { borderColor = it }
        withItIfNotNull(other.borderThickness) { borderThickness = it }
        withItIfNotNull(other.visualLayerIndex) { visualLayerIndex = it }
        return this
    }
}
