package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.DrawableVisualComponent
import com.jafleck.game.components.PositionComponent
import com.jafleck.game.components.RectangleSizeComponent
import ktx.ashley.allOf

inline class DrawableRectangle(val entity: Entity) {
    companion object {

        val family: Family = allOf(
            PositionComponent::class,
            RectangleSizeComponent::class,
            DrawableVisualComponent::class
        ).get()
    }

    val position
        get() = entity[PositionComponent]
    val size
        get() = entity[RectangleSizeComponent]
    val drawableVisual
        get() = entity[DrawableVisualComponent]
}
