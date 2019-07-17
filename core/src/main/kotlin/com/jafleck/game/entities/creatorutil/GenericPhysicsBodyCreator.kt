package com.jafleck.game.entities.creatorutil

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.EarClippingTriangulator
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.BodyComponent
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.basic.VelocityComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.util.box2d.Box2DSettings
import com.jafleck.game.util.logger
import com.jafleck.game.util.math.PolygonType
import com.jafleck.game.util.math.PolygonTypeDetector
import com.jafleck.game.util.math.triangulate
import ktx.box2d.BodyDefinition
import ktx.box2d.FixtureDefinition
import ktx.box2d.body

class GenericPhysicsBodyCreator(
    private val world: World
) {

    private val polygonTriangulator = EarClippingTriangulator()
    private val polygonTypeDetector = PolygonTypeDetector()

    private val logger = logger(this::class)

    fun createStaticBody(entity: Entity, fixtureBlock: FixtureDefinition.() -> Unit) {
        entity.add(BodyComponent(world.body {
            type = BodyDef.BodyType.StaticBody

            val physicsEntity = GenericPhysicsEntity(entity)
            setCommonProperties(physicsEntity)
            determineShape(physicsEntity, fixtureBlock)
        }))
    }

    fun createDynamicBody(entity: Entity, fixtureBlock: FixtureDefinition.() -> Unit) {
        entity.add(BodyComponent(world.body {
            type = BodyDef.BodyType.DynamicBody

            val physicsEntity = GenericPhysicsEntity(entity)
            setCommonProperties(physicsEntity)
            determineShape(physicsEntity, fixtureBlock)
            setVelocity(physicsEntity)
        }))
    }

    fun createKinematicBody(entity: Entity, fixtureBlock: FixtureDefinition.() -> Unit) {
        entity.add(BodyComponent(world.body {
            type = BodyDef.BodyType.KinematicBody

            val physicsEntity = GenericPhysicsEntity(entity)
            setCommonProperties(physicsEntity)
            determineShape(physicsEntity, fixtureBlock)
            setVelocity(physicsEntity)
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
            withItIfNotNull(physicsEntity.polygonShape) {
                val vertices = it.vertices
                val triangulate = polygonTypeDetector.determinePolygonType(vertices) == PolygonType.CONCAVE
                    || (vertices.size / 2 > Box2DSettings.maxPolygonVertices)
                // note potential performance-improvement: create sub-polygons instead of triangles to vastly increase performance
                if (triangulate) {
                    logger.debug { "Polygon is either concave or has more than ${Box2DSettings.maxPolygonVertices} vertices. It has to be triangulated for Box2D." }
                    triangulate(vertices, polygonTriangulator) { triangleVertices ->
                        polygon(triangleVertices) {  // if this fails, your polygon might have vertices on the same line, e.g. 0,0 1,0 2,0
                            fixtureBlock()
                        }
                    }
                } else {
                    polygon(vertices) { // if this fails, your polygon might have vertices on the same line, e.g. 0,0 1,0 2,0
                        fixtureBlock()
                    }
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

    private fun BodyDefinition.setVelocity(physicsEntity: GenericPhysicsEntity) {
        withItIfNotNull(physicsEntity.velocity) {
            linearVelocity.set(it.vector)
        }
    }
}

inline class GenericPhysicsEntity(val entity: Entity) {

    val position
        get() = entity[OriginPositionComponent]
    val velocity
        get() = entity.getOrNull(VelocityComponent)
    val rotation
        get() = entity.getOrNull(RotationComponent)
    val rectangleShape
        get() = entity.getOrNull(RectangleShapeComponent)
    val circleShape
        get() = entity.getOrNull(CircleShapeComponent)
    val polygonShape
        get() = entity.getOrNull(PolygonShapeComponent)
}
