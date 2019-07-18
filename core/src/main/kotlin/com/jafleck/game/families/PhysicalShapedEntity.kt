package com.jafleck.game.families

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.BodyComponent
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.VelocityComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import ktx.ashley.allOf
import ktx.ashley.oneOf

inline class PhysicalShapedEntity(val entity: Entity) {
    companion object {
        // TODO custom Family builder because the Ashley ones do not expose the fields to combine families - also it should be dsl-like
        val family: Family = oneOf(
            RectangleShapeComponent::class,
            CircleShapeComponent::class,
            PolygonShapeComponent::class
        )
            .allOf(
                OriginPositionComponent::class,
                BodyComponent::class
            )
            .get()
    }

    val position
        get() = entity[OriginPositionComponent]
    val velocity
        get() = entity.getOrNull(VelocityComponent)
    val body: BodyComponent
        get() = entity[BodyComponent]

    fun asShapedEntity() = ShapedEntity(entity)
}
