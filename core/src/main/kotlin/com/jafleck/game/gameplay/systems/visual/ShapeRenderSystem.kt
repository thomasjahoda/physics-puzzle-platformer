package com.jafleck.game.gameplay.systems.visual

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.families.VisualShape
import com.jafleck.game.gameplay.ui.GameCamera


class ShapeRenderSystem(
    priority: Int,
    private val camera: GameCamera
) : EntitySystem(priority) {

    private val sr = ShapeRenderer().apply {
        setAutoShapeType(false)
    }
    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(VisualShape.family)
    }

    override fun removedFromEngine(engine: Engine) {
    }

    override fun update(deltaSeconds: Float) {
        sr.projectionMatrix = camera.combined

        for (untypedEntity in entities) {
            val entity = VisualShape(untypedEntity)
            val position = entity.position
            val rotationDegrees = entity.rotation?.degrees ?: 0f
            val renderedShape = entity.renderedShape

            // TODO rotation

            if (renderedShape.fillColor != null) {
                sr.begin(ShapeRenderer.ShapeType.Filled)
                sr.color = renderedShape.fillColor
                drawShape(position, entity)
                sr.end()
            }
            if (renderedShape.borderColor != null) {
                // TODO border thickness
                sr.color = renderedShape.borderColor
                sr.begin(ShapeRenderer.ShapeType.Line)
                drawShape(position, entity)
                sr.end()
            }

            if (rotationDegrees != 0f) {
                sr.identity()
            }
        }
    }

    private fun drawShape(position: OriginPositionComponent, entity: VisualShape) {
        val rectangleShape = entity.rectangleShape
        if (rectangleShape != null) {
            sr.box(position.originX - rectangleShape.width / 2, position.originY - rectangleShape.height / 2, 0f,
                rectangleShape.width, rectangleShape.height, 0f)
        } else {
            val circleShape = entity.circleShape
            if (circleShape != null) {
                sr.circle(position.originX, position.originY, circleShape.radius, 16)
            }
        }
    }

}
