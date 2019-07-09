package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.components.*
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import ktx.box2d.body
import org.koin.dsl.module

inline class ThrownBallEntity(val entity: Entity) {

    companion object {
        val RADIUS: Float = 0.1f
        val SIZE = Vector2(2 * RADIUS, 2 * RADIUS)
        val HALF_SIZE: Vector2 = SIZE.cpy().scl(0.5f)
        const val DENSITY = 10f
        const val FRICTION = 0.2f
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
    private val world: World,
    assetManager: AssetManager,
    screenToWorldScalingPropagator: ScreenToWorldScalingPropagator
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
                borderColor = Color.RED.cpy().mul(0.9f), borderThickness = null,
                fillColor = Color.RED.cpy().mul(0.4f)))
            add(VelocityComponent(velocity))
            add(BodyComponent(world.body {
                type = BodyDef.BodyType.DynamicBody
                circle(radius = ThrownBallEntity.RADIUS) {
                    density = ThrownBallEntity.DENSITY
                    friction = ThrownBallEntity.FRICTION
                    restitution = 0.5f
                }
                linearVelocity.set(velocity)
                this.position.set(originPosition)
            }))
            add(ThrownBallComponent())
        }
        engine.addEntity(entity)
        return ThrownBallEntity(entity)
    }
}

val thrownBallModule = module {
    single { ThrownBallEntityCreator(get(), get(), get(), get()) }
}
