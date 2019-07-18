package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.components.shape.ShapeComponent
import ktx.ashley.allOf
import ktx.ashley.oneOf

inline class ShapedEntity(val entity: Entity) {
    companion object {
        val family: Family = oneOf(
            RectangleShapeComponent::class,
            CircleShapeComponent::class,
            PolygonShapeComponent::class
        )
            .allOf(OriginPositionComponent::class)
            .get()

    }

    val position
        get() = entity[OriginPositionComponent]
    val rotation
        get() = entity.getOrNull(RotationComponent)
    val rectangleShape: RectangleShapeComponent?
        get() = entity.getOrNull(RectangleShapeComponent)
    val circleShape: CircleShapeComponent?
        get() = entity.getOrNull(CircleShapeComponent)
    val polygonShape: PolygonShapeComponent?
        get() = entity.getOrNull(PolygonShapeComponent)
    val shape: ShapeComponent
        get() {
            withItIfNotNull(rectangleShape) { return it }
            withItIfNotNull(circleShape) { return it }
            withItIfNotNull(polygonShape) { return it }
            error("unknown shape")
        }
}
