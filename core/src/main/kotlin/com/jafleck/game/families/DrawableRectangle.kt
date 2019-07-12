package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.visual.DrawableVisualComponent
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.visual.RectangleBoundsComponent
import com.jafleck.game.components.basic.RotationComponent
import ktx.ashley.allOf

inline class DrawableRectangle(val entity: Entity) {
    companion object {
        val family: Family = allOf(
            OriginPositionComponent::class,
            RectangleBoundsComponent::class,
            DrawableVisualComponent::class
        ).get()
    }

    val position
        get() = entity[OriginPositionComponent]
    val size
        get() = entity[RectangleBoundsComponent]
    val drawableVisual
        get() = entity[DrawableVisualComponent]
    val rotation: RotationComponent?
        get() = entity.getOrNull(RotationComponent)
}
