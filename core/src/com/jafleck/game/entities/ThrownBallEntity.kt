package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.Assets
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.assets.autoScaleByExpectedWorldSize
import com.jafleck.game.components.*
import com.jafleck.game.families.DrawableRectangle
import com.jafleck.game.families.MovingBody
import ktx.box2d.body

inline class ThrownBallEntity(val entity: Entity) {

    companion object {
        val SIZE = Vector2(0.2f, 0.2f)
        val HALF_SIZE: Vector2 = SIZE.cpy().scl(0.5f)
        const val DENSITY = 10f
        const val FRICTION = 0.2f
        val COLOR: Color = Color.RED.cpy().mul(0.4f)
    }

    fun asDrawableRectangle() = DrawableRectangle(entity)
    fun asMovingBody() = MovingBody(entity)

    val position
        get() = entity[OriginPositionComponent]
    val size
        get() = entity[RectangleSizeComponent]
    val drawableVisual
        get() = entity[DrawableVisualComponent]
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
    private val platformNinePatch = assetManager.get(Assets.atlas).createPatch("ugly-ball").apply {
        color = ThrownBallEntity.COLOR
    }
    private val drawable: Drawable = NinePatchDrawable(platformNinePatch).apply {
        autoScaleByExpectedWorldSize(screenToWorldScalingPropagator, ThrownBallEntity.SIZE)
    }

    fun createThrownBall(
        originPosition: Vector2,
        velocity: Vector2
    ): ThrownBallEntity {
        val entity = engine.createEntity().apply {
            add(OriginPositionComponent(originPosition))
            add(RectangleSizeComponent(ThrownBallEntity.SIZE))
            add(DrawableVisualComponent(drawable))
            add(VelocityComponent(velocity))
            add(BodyComponent(world.body {
                type = BodyDef.BodyType.DynamicBody
                circle(radius = ThrownBallEntity.HALF_SIZE.x) {
                    density = ThrownBallEntity.DENSITY
                    friction = ThrownBallEntity.FRICTION
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
