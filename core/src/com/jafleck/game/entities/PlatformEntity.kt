package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.Assets
import com.jafleck.game.components.DrawableVisualComponent
import com.jafleck.game.components.PlatformComponent
import com.jafleck.game.components.PositionComponent
import com.jafleck.game.components.RectangleSizeComponent
import com.jafleck.game.families.DrawableRectangle

inline class PlatformEntity(val entity: Entity) {

    companion object {
        val COLOR = Color.GREEN
    }

    fun asDrawableRectangle() = DrawableRectangle(entity)

    val position
        get() = entity[PositionComponent]
    val size
        get() = entity[RectangleSizeComponent]
    val drawableVisual
        get() = entity[DrawableVisualComponent]
    val platform
        get() = entity[PlatformComponent]
}

class PlatformEntityCreator(
    private val engine: Engine,
    private val assetManager: AssetManager
) {
    private val platformNinePatch = assetManager.get(Assets.atlas).createPatch("platform").apply {
        color = PlatformEntity.COLOR
    }
    private val drawable: Drawable = NinePatchDrawable(platformNinePatch)

    fun createPlatformEntity(
        rectangle: Rectangle
    ): PlatformEntity {
        val entity = engine.createEntity().apply {
            add(PositionComponent(rectangle.x, rectangle.y))
            add(RectangleSizeComponent(rectangle.width, rectangle.height))
            add(DrawableVisualComponent(drawable))
            add(PlatformComponent())
        }
        engine.addEntity(entity)
        return PlatformEntity(entity)
    }
}
