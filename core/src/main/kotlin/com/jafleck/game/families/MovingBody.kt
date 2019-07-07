package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.BodyComponent
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.VelocityComponent
import ktx.ashley.allOf

inline class MovingBody(val entity: Entity) {
    companion object {
        val family: Family = allOf(
            OriginPositionComponent::class,
            VelocityComponent::class,
            BodyComponent::class
        ).get()
    }

    val position
        get() = entity[OriginPositionComponent]
    val body
        get() = entity[BodyComponent]
    val velocity
        get() = entity[VelocityComponent]
}
