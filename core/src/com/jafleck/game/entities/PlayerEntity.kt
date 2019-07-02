package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.Assets
import com.jafleck.game.components.*
import com.jafleck.game.families.DrawableRectangle
import ktx.box2d.body

inline class PlayerEntity(val entity: Entity) {

    companion object {
        val SIZE = Vector2(100f, 100f)
        val HALF_SIZE: Vector2 = Vector2(SIZE).scl(0.5f)
        const val DENSITY = 10f
        const val FRICTION = 0.2f
        val COLOR: Color = Color.RED
    }

    fun asDrawableRectangle() = DrawableRectangle(entity)

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
}

class PlayerEntityCreator(
    private val engine: Engine,
    private val assetManager: AssetManager,
    private val world: World
) {
    private val playerTextureRegion = assetManager.get(Assets.atlas).findRegion("player")
    private val drawable: Drawable = TextureRegionDrawable(playerTextureRegion).tint(PlayerEntity.COLOR)

    fun createPlayerEntity(
        lowerLeftCornerPosition: Vector2
    ): PlayerEntity {
        val entity = engine.createEntity().apply {
            val originPosition = Vector2(lowerLeftCornerPosition).add(PlayerEntity.HALF_SIZE)
            add(OriginPositionComponent(originPosition))
            add(RectangleSizeComponent(PlayerEntity.SIZE))
            add(DrawableVisualComponent(drawable))
            add(VelocityComponent(0f, 0f))
            add(BodyComponent(world.body {
                type = BodyDef.BodyType.DynamicBody
                box(PlayerEntity.SIZE.x, PlayerEntity.SIZE.y) {
                    density = PlayerEntity.DENSITY
                    friction = PlayerEntity.FRICTION
                }
                this.position.set(originPosition)
            }))
            add(PlayerComponent())
        }
        engine.addEntity(entity)
        return PlayerEntity(entity)
    }
}
