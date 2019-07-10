package com.jafleck.game.entities.creatorutil

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.BodyComponent
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.RotationComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import ktx.box2d.BodyDefinition
import ktx.box2d.body

class GenericPhysicsBodyCreator(
    private val world: World
) {

    fun createStaticBody(entity: Entity) {
        entity.add(BodyComponent(world.body {
            type = BodyDef.BodyType.StaticBody

            val physicsEntity = GenericPhysicsEntity(entity)
            determineShape(physicsEntity)

            position.set(physicsEntity.position.vector)

            withItIfNotNull(physicsEntity.rotation) {
                angle = it.radians
            }
        }))
    }

    private fun BodyDefinition.determineShape(physicsEntity: GenericPhysicsEntity) {
        when {
            withItIfNotNull(physicsEntity.rectangleShape) {
                box(it.width, it.height) {

                }
            } -> {
            }
            withItIfNotNull(physicsEntity.circleShape) {
                circle(it.radius) {

                }
            } -> {
            }
            else -> error("unknown shape")
        }
    }
}

inline class GenericPhysicsEntity(val entity: Entity) {

    val position
        get() = entity[OriginPositionComponent]
    val rotation
        get() = entity.getOrNull(RotationComponent)
    val rectangleShape
        get() = entity.getOrNull(RectangleShapeComponent)
    val circleShape
        get() = entity.getOrNull(CircleShapeComponent)
}
