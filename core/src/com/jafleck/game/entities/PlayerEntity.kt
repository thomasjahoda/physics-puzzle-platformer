package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.Assets
import com.jafleck.game.components.DrawableVisualComponent
import com.jafleck.game.components.PlayerComponent
import com.jafleck.game.components.PositionComponent
import com.jafleck.game.components.RectangleSizeComponent
import com.jafleck.game.families.DrawableRectangle

inline class PlayerEntity(val entity: Entity) {

    companion object {
        val SIZE = Vector2(100f, 100f)
        val COLOR = Color.RED
    }

    fun asDrawableRectangle() = DrawableRectangle(entity)

    val position
        get() = entity[PositionComponent]
    val size
        get() = entity[RectangleSizeComponent]
    val drawableVisual
        get() = entity[DrawableVisualComponent]
    val player
        get() = entity[PlayerComponent]
}

class PlayerEntityCreator(
    private val engine: Engine,
    private val assetManager: AssetManager
) {
    private val playerTextureRegion = assetManager.get(Assets.atlas).findRegion("player")
    private val drawable: Drawable = TextureRegionDrawable(playerTextureRegion).tint(PlayerEntity.COLOR)

    fun createPlayerEntity(
        position: Vector2
    ): PlayerEntity {
        val entity = engine.createEntity().apply {
            add(PositionComponent(position))
            add(RectangleSizeComponent(PlayerEntity.SIZE))
            add(DrawableVisualComponent(drawable))
            add(PlayerComponent())
        }
        engine.addEntity(entity)
        return PlayerEntity(entity)
    }
}
