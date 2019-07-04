package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.DrawableVisualComponent
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.RectangleSizeComponent
import com.jafleck.game.components.RotationComponent
import ktx.ashley.allOf

inline class DrawableRectangle(val entity: Entity) {
    companion object {
        val family: Family = allOf(
            OriginPositionComponent::class,
            RectangleSizeComponent::class,
            DrawableVisualComponent::class
        ).get()
    }

    val position
        get() = entity[OriginPositionComponent]
    val size
        get() = entity[RectangleSizeComponent]
    val drawableVisual
        get() = entity[DrawableVisualComponent]
    val rotation: RotationComponent?
        get() = entity.getOrNull(RotationComponent)
}
