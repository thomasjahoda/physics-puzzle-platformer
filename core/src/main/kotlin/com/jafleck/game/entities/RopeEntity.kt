package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Filter
import com.jafleck.extensions.libgdx.graphics.mulExceptAlpha
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.basic.VelocityComponent
import com.jafleck.game.components.entities.RopeComponent
import com.jafleck.game.components.entities.RopePartComponent
import com.jafleck.game.components.entities.StickyRopePartComponent
import com.jafleck.game.components.entities.ThrownRopeComponent
import com.jafleck.game.components.logic.ThrowerOfRopeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.physics.CollisionEntityCategory
import com.jafleck.game.families.PhysicalShapedEntity
import ktx.ashley.allOf
import ktx.box2d.Box2DDsl
import ktx.box2d.filter
import ktx.box2d.ropeJointWith
import ktx.math.minus
import ktx.math.times
import org.koin.dsl.module
import java.util.*

inline class RopeEntity(val entity: Entity) {

    companion object {
        val family: Family = allOf(
            RopeComponent::class
        )
            .get()
    }

    val rope
        get() = entity[RopeComponent]

    fun asThrownRope(): ThrownRopeEntity? {
        if (entity.getOrNull(ThrownRopeComponent) != null) {
            return ThrownRopeEntity(entity)
        }
        return null
    }
}

inline class ThrownRopeEntity(val entity: Entity) {

    companion object {
        val family: Family = allOf(
            RopeComponent::class,
            ThrownRopeComponent::class
        )
            .get()

        const val MAX_LENGTH_IN_PARTS = 50
    }

    val rope
        get() = entity[RopeComponent]
    val thrownRope
        get() = entity[ThrownRopeComponent]

    fun asRopeEntity() = RopeEntity(entity)
}

inline class RopePartEntity(val entity: Entity) {

    companion object {
        val family: Family = allOf(
            RopePartComponent::class
        ).get()

        val ROPE_PART_SIZE = Vector2(0.4f, 0.4f)
        const val TOP_PART_DENSITY = 8f
        const val NORMAL_PART_DENSITY = 0.5f

        const val PHYSICS_GROUP_INDEX = 1
    }

    val ropePart
        get() = entity[RopePartComponent]
    val velocity
        get() = entity[VelocityComponent]
    val stickyRopePart
        get() = entity.getOrNull(StickyRopePartComponent)


    fun asPhysicalShapedEntity() = PhysicalShapedEntity(entity)
}

class RopeEntityCreator(
    private val engine: Engine,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator
) {
    fun createRopeByBeingThrown(
        thrower: PhysicalShapedEntity,
        localAnchorBodyPosition: Vector2,
        originPosition: Vector2,
        velocity: Vector2
    ): ThrownRopeEntity {
        val ropeParts = LinkedList<Entity>()
        val thrownRopeEntity = ThrownRopeEntity(engine.createEntity().apply {
            add(RopeComponent(ropeParts))
            add(ThrownRopeComponent(thrower, localAnchorBodyPosition, ThrownRopeEntity.MAX_LENGTH_IN_PARTS, null))
        })

        val ropePart = createTopRopePart(originPosition, velocity, thrownRopeEntity.asRopeEntity())
        ropeParts.add(ropePart)
        engine.addEntity(ropePart)

        engine.addEntity(thrownRopeEntity.entity)

        thrower.entity.add(ThrowerOfRopeComponent(thrownRopeEntity))
        return thrownRopeEntity
    }

    fun createNormalRopePartAttachedTo(ropeOriginatingFromPosition: Vector2, ropePartToAttachTo: RopePartEntity, velocity: Vector2): RopePartEntity {
//        VisualDebugMarkerEntityCreator.instance.createMarker(ropeOriginatingFromPosition, Color.YELLOW, "ropeOriginatingFromPosition")
        val ropeEntity = ropePartToAttachTo.ropePart.owningRopeEntity
        val bodyToAttachTo = ropePartToAttachTo.asPhysicalShapedEntity().body.value
        val targetPartLocalAnchorPosition = ropePartToAttachTo.asPhysicalShapedEntity().asShapedEntity().shape.getRectangleAroundShape(Vector2()).let {
            Vector2(-it.x / 2, 0f) // this is the unrotated connection point -> we connect to the left point of the shape
        }

        // create new part from next part in direction to originating position
        val newPartTopEdgeMiddlePointPosition = bodyToAttachTo.getWorldPoint(targetPartLocalAnchorPosition)
        val ropePartLength = RopePartEntity.ROPE_PART_SIZE.y
        val directionFromRopeOriginatingPositionToNextPartTopEdgePosition = (newPartTopEdgeMiddlePointPosition - ropeOriginatingFromPosition).nor()
        val originPosition = newPartTopEdgeMiddlePointPosition - (directionFromRopeOriginatingPositionToNextPartTopEdgePosition * (ropePartLength / 2))

//        VisualDebugMarkerEntityCreator.instance.createMarkerFromCurrentEntity(ropePartToAttachTo.entity, Color.GREEN.cpyWithAlpha(0.5f), "ropePartToAttachTo")
//        VisualDebugMarkerEntityCreator.instance.createMarker(newPartTopEdgeMiddlePointPosition, Color.BLUE.cpyWithAlpha(0.5f), "newPartTopEdgeMiddlePointPosition")
//        VisualDebugMarkerEntityCreator.instance.createMarker(originPosition, Color.VIOLET.cpyWithAlpha(0.5f), "originPosition")
        val rotationDegrees = directionFromRopeOriginatingPositionToNextPartTopEdgePosition.angle()
        val newRopePart = createNormalRopePart(originPosition, velocity, rotationDegrees, ropeEntity)
//        VisualDebugMarkerEntityCreator.instance.createMarkerFromCurrentEntity(newRopePart.entity, Color.GOLD.cpyWithAlpha(0.5f), "ropePartToAttachTo")
        ropeEntity.rope.parts.add(0, newRopePart.entity)
        engine.addEntity(newRopePart.entity)

        // attach to next part
        newRopePart.ropePart.restrictedToNextPartBy = bodyToAttachTo.ropeJointWith(newRopePart.asPhysicalShapedEntity().body.value) {
            localAnchorA.set(Vector2.Zero)
            localAnchorB.set(Vector2.Zero)
            maxLength = ropePartLength
        }

        return newRopePart
    }

    private fun createNormalRopePart(originPosition: Vector2, velocity: Vector2, rotationDegrees: Float, ropeEntity: RopeEntity): RopePartEntity {
        return engine.createEntity().apply {
            add(OriginPositionComponent(originPosition))
            add(RotationComponent.fromDegrees(rotationDegrees))
            add(RectangleShapeComponent(RopePartEntity.ROPE_PART_SIZE))
            add(VisualShapeComponent(
                borderColor = Color.CORAL.cpy().mulExceptAlpha(0.9f), borderThickness = RopePartEntity.ROPE_PART_SIZE.x / 6,
                fillColor = Color.BROWN.cpy().mulExceptAlpha(0.4f)))
            add(VelocityComponent(velocity))
            genericPhysicsBodyCreator.createDynamicBody(this) {
                density = RopePartEntity.NORMAL_PART_DENSITY
                friction = 0.02f
                restitution = 0.05f
                filter {
                    doNotCollideWithRopeParts()
                    maskBits = CollisionEntityCategory.environment
                }
            }
            add(RopePartComponent(ropeEntity, null, null))
        }.let { RopePartEntity(it) }
    }

    private fun createTopRopePart(originPosition: Vector2, velocity: Vector2, ropeEntity: RopeEntity): Entity {
        val rotationDegrees = velocity.angle()
        return engine.createEntity().apply {
            add(OriginPositionComponent(originPosition))
            add(RotationComponent.fromDegrees(rotationDegrees))
            add(RectangleShapeComponent(RopePartEntity.ROPE_PART_SIZE))
            add(VisualShapeComponent(
                borderColor = Color.CHARTREUSE.cpy().mulExceptAlpha(0.9f), borderThickness = RopePartEntity.ROPE_PART_SIZE.x / 6,
                fillColor = Color.BROWN.cpy().mulExceptAlpha(0.4f)))
            add(VelocityComponent(velocity))
            genericPhysicsBodyCreator.createDynamicBody(this) {
                density = RopePartEntity.TOP_PART_DENSITY
                friction = 0.02f
                restitution = 0.05f
                filter {
                    doNotCollideWithRopeParts()
                    maskBits = CollisionEntityCategory.environment
                }
            }
            add(RopePartComponent(ropeEntity, null, null))
            add(StickyRopePartComponent(null, null))
        }
    }

    private fun @Box2DDsl Filter.doNotCollideWithRopeParts() {
        groupIndex = (RopePartEntity.PHYSICS_GROUP_INDEX * -1).toShort()
    }
}

val ropeModule = module {
    single { RopeEntityCreator(get(), get()) }
}
