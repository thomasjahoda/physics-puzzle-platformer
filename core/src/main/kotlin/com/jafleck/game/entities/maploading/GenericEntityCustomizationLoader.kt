package com.jafleck.game.entities.maploading

import com.badlogic.gdx.maps.MapObject
import com.jafleck.game.entities.customizations.GenericEntityCustomization
import com.jafleck.game.util.libgdx.map.*

class GenericEntityCustomizationLoader {

    fun load(mapObject: MapObject): GenericEntityCustomization {
        return GenericEntityCustomization(
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
