package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.*
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import org.koin.dsl.module

inline class ThrownBallEntity(val entity: Entity) {

    companion object {
        val RADIUS: Float = 0.5f
        val SIZE = Vector2(2 * RADIUS, 2 * RADIUS)
        val HALF_SIZE: Vector2 = SIZE.cpy().scl(0.5f)
    }

    val position
        get() = entity[OriginPositionComponent]
    val size
        get() = entity[RectangleShapeComponent]
    val player
        get() = entity[PlayerComponent]
    val body
        get() = entity[BodyComponent]

    fun addLimitedDuration(secondsLeft: Float) {
        entity.add(RemoveAfterDurationComponent(secondsLeft))
    }
}

class ThrownBallEntityCreator(
    private val engine: Engine,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator
) {
    fun createThrownBall(
        originPosition: Vector2,
        velocity: Vector2
    ): ThrownBallEntity {
        val entity = engine.createEntity().apply {
            add(OriginPositionComponent(originPosition))
            add(CircleShapeComponent(ThrownBallEntity.RADIUS))
            add(RectangleBoundsComponent(ThrownBallEntity.SIZE))
            add(RotationComponent(0f))
            add(VisualShapeComponent(
                borderColor = Color.RED.cpy().mul(0.9f), borderThickness = ThrownBallEntity.RADIUS / 3,
                fillColor = Color.RED.cpy().mul(0.4f)))
            add(VelocityComponent(velocity))
            genericPhysicsBodyCreator.createDynamicBody(this) {
                density = 4f
                friction = 0.2f
                restitution = 0.5f
            }
            add(ThrownBallComponent())
        }
        engine.addEntity(entity)
        return ThrownBallEntity(entity)
    }
}

val thrownBallModule = module {
    single { ThrownBallEntityCreator(get(), get()) }
}
