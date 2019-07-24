package com.jafleck.game.entities.maploading

import com.badlogic.gdx.maps.MapObject
import com.jafleck.game.entities.config.GenericEntityConfig
import com.jafleck.game.util.libgdx.maps.*

class GenericEntityCustomizationLoader {

    fun load(mapObject: MapObject): GenericEntityConfig {
        return GenericEntityConfig(
            initialVelocity = mapObject.initialVelocity,
            density = mapObject.customDensity,
            friction = mapObject.customFriction,
            restitution = mapObject.customRestitution,
            linearDamping = mapObject.customLinearDamping,
            angularDamping = mapObject.customAngularDamping,
            gravityScale = mapObject.customGravityScale,
            fixedRotation = mapObject.customFixedRotation,
            fillColor = mapObject.customFillColor,
            borderColor = mapObject.customBorderColor,
            borderThickness = mapObject.customBorderThickness
        )
    }
}
