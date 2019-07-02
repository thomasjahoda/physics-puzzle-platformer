package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.Assets
import com.jafleck.game.components.*
import com.jafleck.game.families.DrawableRectangle
import ktx.box2d.body

inline class PlatformEntity(val entity: Entity) {

    companion object {
        val COLOR = Color.GREEN
    }

    fun asDrawableRectangle() = DrawableRectangle(entity)

    val position
        get() = entity[OriginPositionComponent]
    val size
        get() = entity[RectangleSizeComponent]
    val drawableVisual
        get() = entity[DrawableVisualComponent]
    val platform
        get() = entity[PlatformComponent]
}

class PlatformEntityCreator(
    private val engine: Engine,
    private val assetManager: AssetManager,
    private val world: World
) {
    private val platformNinePatch = assetManager.get(Assets.atlas).createPatch("platform").apply {
        color = PlatformEntity.COLOR
    }
    private val drawable: Drawable = NinePatchDrawable(platformNinePatch)

    fun createPlatformEntity(
        rectangle: Rectangle
    ): PlatformEntity {
        val entity = engine.createEntity().apply {
            val originPosition = rectangle.getCenter(Vector2())
            add(OriginPositionComponent(originPosition))
            add(RectangleSizeComponent(rectangle.getSize(Vector2())))
            add(DrawableVisualComponent(drawable))
            add(BodyComponent(world.body {
                type = BodyDef.BodyType.StaticBody
                box(rectangle.width, rectangle.height) {
                    density = PlayerEntity.DENSITY
                    friction = PlayerEntity.FRICTION
                }
                position.set(originPosition)
            }))
            add(PlatformComponent())
        }
        engine.addEntity(entity)
        return PlatformEntity(entity)
    }
}
