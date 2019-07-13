package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.components.shape.ShapeComponent
import ktx.ashley.allOf

inline class ShapedEntity(val entity: Entity) {
    companion object {
        val family: Family = allOf(
            RectangleShapeComponent::class,
            CircleShapeComponent::class
        ).get()
    }

    val position
        get() = entity[OriginPositionComponent]
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
