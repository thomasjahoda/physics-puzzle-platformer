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
import ktx.box2d.FixtureDefinition
import ktx.box2d.body

class GenericPhysicsBodyCreator(
    private val world: World
) {

    fun createStaticBody(entity: Entity) {
        entity.add(BodyComponent(world.body {
            type = BodyDef.BodyType.StaticBody

            val physicsEntity = GenericPhysicsEntity(entity)
            setCommonProperties(physicsEntity)
            determineShape(physicsEntity) {}
        }))
    }

    fun createDynamicBody(entity: Entity, fixtureBlock: FixtureDefinition.() -> Unit) {
        entity.add(BodyComponent(world.body {
            type = BodyDef.BodyType.DynamicBody

            val physicsEntity = GenericPhysicsEntity(entity)
            setCommonProperties(physicsEntity)
            determineShape(physicsEntity, fixtureBlock)
        }))
    }

    fun createKinematicBody(entity: Entity, fixtureBlock: FixtureDefinition.() -> Unit) {
        entity.add(BodyComponent(world.body {
            type = BodyDef.BodyType.DynamicBody

            val physicsEntity = GenericPhysicsEntity(entity)
            setCommonProperties(physicsEntity)
            determineShape(physicsEntity, fixtureBlock)
        }))
    }

    private fun BodyDefinition.setCommonProperties(physicsEntity: GenericPhysicsEntity) {
        storeUserData(physicsEntity)
        setPosition(physicsEntity.position)
        setRotation(physicsEntity)
    }

    private fun BodyDefinition.determineShape(physicsEntity: GenericPhysicsEntity, fixtureBlock: FixtureDefinition.() -> Unit) {
        when {
            withItIfNotNull(physicsEntity.rectangleShape) {
                box(it.width, it.height) {
                    fixtureBlock()
                }
            } -> {
            }
            withItIfNotNull(physicsEntity.circleShape) {
                circle(it.radius) {
                    fixtureBlock()
                }
            } -> {
            }
            else -> error("unknown shape")
        }
    }

    private fun BodyDefinition.storeUserData(physicsEntity: GenericPhysicsEntity) {
        userData = physicsEntity.entity
    }

    private fun BodyDefinition.setPosition(originPositionComponent: OriginPositionComponent) {
        position.set(originPositionComponent.vector)
    }

    private fun BodyDefinition.setRotation(physicsEntity: GenericPhysicsEntity) {
        withItIfNotNull(physicsEntity.rotation) {
            angle = it.radians
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