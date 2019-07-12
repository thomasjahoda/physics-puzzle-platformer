package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import ktx.ashley.allOf
import ktx.ashley.oneOf

inline class VisualShape(val entity: Entity) {
    companion object {
        val family: Family = allOf(
            OriginPositionComponent::class,
            VisualShapeComponent::class
        ).oneOf(
            RectangleShapeComponent::class,
            CircleShapeComponent::class,
            PolygonShapeComponent::class
        ).get()
    }

    val position
        get() = entity[OriginPositionComponent]
    val renderedShape
        get() = entity[VisualShapeComponent]
    val rotation: RotationComponent?
        get() = entity.getOrNull(RotationComponent)

    val rectangleShape: RectangleShapeComponent?
        get() = entity.getOrNull(RectangleShapeComponent)
    val circleShape: CircleShapeComponent?
        get() = entity.getOrNull(CircleShapeComponent)
    val polygonShape: PolygonShapeComponent?
        get() = entity.getOrNull(PolygonShapeComponent)
}
