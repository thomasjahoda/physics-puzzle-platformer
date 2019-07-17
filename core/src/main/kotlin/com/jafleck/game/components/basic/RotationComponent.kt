package com.jafleck.game.components.basic

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.MathUtils
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class RotationComponent(
    var radians: Float
) : Component {

    var degrees
        get() = radians * MathUtils.radiansToDegrees
        set(value) {
            radians = value * MathUtils.degreesToRadians
        }

    companion object : ComponentMapperAccessor<RotationComponent>(RotationComponent::class) {
        fun fromDegrees(degrees: Float): RotationComponent {
            return RotationComponent(degrees * MathUtils.degreesToRadians)
        }
    }
}
