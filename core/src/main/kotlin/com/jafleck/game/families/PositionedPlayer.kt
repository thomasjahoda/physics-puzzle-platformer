package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.PlayerComponent
import ktx.ashley.allOf

inline class PositionedPlayer(val entity: Entity) {
    companion object {
        val family: Family = allOf(
            OriginPositionComponent::class,
            PlayerComponent::class
        ).get()
    }

    val position
        get() = entity[OriginPositionComponent]
    val player
        get() = entity[PlayerComponent]
}
