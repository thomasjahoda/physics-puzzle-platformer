package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.BodyComponent
import com.jafleck.game.components.RotationComponent
import ktx.ashley.allOf

inline class RotatingBody(val entity: Entity) {
    companion object {
        val family: Family = allOf(
            RotationComponent::class,
            BodyComponent::class
        ).get()
    }

    val body
        get() = entity[BodyComponent]
    val rotation
        get() = entity[RotationComponent]
}
