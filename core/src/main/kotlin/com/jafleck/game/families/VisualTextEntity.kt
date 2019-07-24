package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.visual.VisualTextComponent
import ktx.ashley.allOf

inline class VisualTextEntity(val entity: Entity) {
    companion object {
        val family: Family = allOf(
            OriginPositionComponent::class,
            VisualTextComponent::class
        ).get()
    }

    fun asShapedEntity() = ShapedEntity(entity)

    val position
        get() = entity[OriginPositionComponent]
    val visualText
        get() = entity[VisualTextComponent]
    val rotation: RotationComponent?
        get() = entity.getOrNull(RotationComponent)
}
