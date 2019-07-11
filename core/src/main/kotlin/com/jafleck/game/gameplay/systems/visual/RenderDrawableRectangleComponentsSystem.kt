package com.jafleck.game.gameplay.systems.visual

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.families.DrawableRectangle
import com.jafleck.game.gameplay.ui.GameCamera


class RenderDrawableRectangleComponentsSystem(
    private val spriteBatch: SpriteBatch,
    private val screenToWorldScalingPropagator: ScreenToWorldScalingPropagator,
    private val camera: GameCamera
) : EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(DrawableRectangle.family)
    }

    override fun removedFromEngine(engine: Engine) {
    }

    override fun update(deltaSeconds: Float) {
        screenToWorldScalingPropagator.scaling = Vector2(camera.combined.scaleX, camera.combined.scaleY)

        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()

        for (untypedEntity in entities) {
            val entity = DrawableRectangle(untypedEntity)
            val drawable = entity.drawableVisual.drawable
            val position = entity.position
            val size = entity.size
            val leftBottomX = position.originX - size.width / 2f
            val leftBottomY = position.originY - size.height / 2f
            if (drawable is TransformDrawable) {
                val rotationDegrees = entity.rotation?.degrees ?: 0f
                drawable.draw(spriteBatch,
                    leftBottomX,
                    leftBottomY,
                    size.width / 2f,
                    size.height / 2f,
                    size.width,
                    size.height,
                    1f,
                    1f,
                    rotationDegrees)
            } else {
                drawable.draw(spriteBatch,
                    leftBottomX,
                    leftBottomY,
                    size.width,
                    size.height)
            }
        }

        spriteBatch.end()
    }

}
