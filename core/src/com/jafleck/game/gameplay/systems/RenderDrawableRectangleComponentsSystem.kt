package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jafleck.game.families.DrawableRectangle


class RenderDrawableRectangleComponentsSystem(
    priority: Int,
    private val spriteBatch: SpriteBatch,
    private val camera: OrthographicCamera) : EntitySystem(priority) {

    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(DrawableRectangle.family)
    }

    override fun removedFromEngine(engine: Engine) {
    }

    override fun update(deltaSeconds: Float) {
        camera.update()

        spriteBatch.begin()
        spriteBatch.projectionMatrix = camera.combined

        for (untypedEntity in entities) {
            val entity = DrawableRectangle(untypedEntity)
            val drawable = entity.drawableVisual.drawable
            val position = entity.position
            val size = entity.size
            drawable.draw(spriteBatch, position.x, position.y, size.width, size.height)
        }

        spriteBatch.end()
    }

}
