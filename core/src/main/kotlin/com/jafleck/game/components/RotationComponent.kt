package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.MathUtils
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class RotationComponent(
    var radians: Float
) : Component {

    var degrees
        get() = radians * MathUtils.radiansToDegrees
        set(value) {
            radians = value * MathUtils.degreesToRadians
        }

    companion object : ComponentMapperAccessor<RotationComponent>(RotationComponent::class)
}
