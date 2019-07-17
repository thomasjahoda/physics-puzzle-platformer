package com.jafleck.game.gameplay.systems.visual

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.jafleck.extensions.libgdx.math.RectanglePolygon
import com.jafleck.extensions.libgdx.rendering.box
import com.jafleck.extensions.libgdx.rendering.circle
import com.jafleck.extensions.libgdx.rendering.fillRectanglePolygon
import com.jafleck.extensions.libgdx.rendering.triangles
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.components.visual.TriangulatedVisualPolygonComponent
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.families.VisualShape
import com.jafleck.game.gameplay.ui.GameCamera
import com.jafleck.game.util.logger


class ShapeRenderSystem(
    private val camera: GameCamera
) : EntitySystem() {

    private val sr = ShapeRenderer().apply {
        setAutoShapeType(false)
    }
    private lateinit var entities: ImmutableArray<Entity>
    private val rectTempPolygon = RectanglePolygon.create()

    private val logger = logger(this::class)

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(VisualShape.family)
    }

    override fun removedFromEngine(engine: Engine) {
    }

    override fun update(deltaSeconds: Float) {
        sr.projectionMatrix = camera.combined

        sr.begin(ShapeRenderer.ShapeType.Filled)
        for (untypedEntity in entities) {
            val entity = VisualShape(untypedEntity)
            val position = entity.position
            val renderedShape = entity.renderedShape
            val borderThickness = renderedShape.borderThickness
            val rotationDegrees = entity.rotation?.degrees ?: 0f

            val circleShape = entity.circleShape
            if (circleShape != null) {
                drawCircle(circleShape, position, renderedShape, borderThickness)
            }

            val rectangleShape = entity.rectangleShape
            if (rectangleShape != null) {
                drawRectangle(rectangleShape, position, renderedShape, rotationDegrees, borderThickness)
            }

            val polygonShape = entity.polygonShape
            if (polygonShape != null) {
                val triangulatedVisualPolygon = entity.triangulatedVisualPolygon!!
                drawPolygon(triangulatedVisualPolygon, position, renderedShape, rotationDegrees, borderThickness)
            }

        }
        sr.end()
    }

    private fun drawCircle(circleShape: CircleShapeComponent, position: OriginPositionComponent, renderedShape: VisualShapeComponent, borderThickness: Float?) {
        if (borderThickness != null) {
            require(renderedShape.fillColor != null) { "Circle with border but without fill color is currently not supported" }
            sr.color = renderedShape.borderColor
            sr.circle(position.vector, circleShape.radius, camera)
            sr.color = renderedShape.fillColor
            sr.circle(position.vector, circleShape.radius - borderThickness, camera)
        } else if (renderedShape.fillColor != null) {
            sr.color = renderedShape.fillColor
            sr.circle(position.vector, circleShape.radius, camera)
        }
    }

    private fun drawRectangle(rectangleShape: RectangleShapeComponent, position: OriginPositionComponent, renderedShape: VisualShapeComponent, rotationDegrees: Float, borderThickness: Float?) {
        if (rotationDegrees == 0f) {
            if (borderThickness != null) {
                // rectangle without rotation but border
                require(renderedShape.fillColor != null) { "Rectangle with border but without fill color is currently not supported" }

                // first fill everything with border color
                sr.color = renderedShape.borderColor
                sr.box(position.vector, rectangleShape.vector)

                // then fill inner rectangle (where border is excluded) with fill color
                sr.color = renderedShape.fillColor
                sr.box(position.vector, rectangleShape.vector, borderThickness)
            } else if (renderedShape.fillColor != null) {
                // simple filled rectangle
                sr.color = renderedShape.fillColor
                sr.box(position.vector, rectangleShape.vector)
            }
        } else {
            if (borderThickness != null) {
                // rectangle without rotation but border
                require(renderedShape.fillColor != null) { "Rectangle with border but without fill color is currently not supported" }

                // first fill everything with border color
                rectTempPolygon.setRectangleShapeAround00(rectangleShape.width, rectangleShape.height)
                rectTempPolygon.polygon.rotation = rotationDegrees
                rectTempPolygon.polygon.setPosition(position.originX, position.originY)
                sr.color = renderedShape.borderColor
                sr.fillRectanglePolygon(rectTempPolygon)

                // then fill inner rectangle (where border is excluded) with fill color
                rectTempPolygon.setRectangleShapeAround00(rectangleShape.width - 2 * borderThickness, rectangleShape.height - 2 * borderThickness)
                rectTempPolygon.polygon.rotation = rotationDegrees
                rectTempPolygon.polygon.setPosition(position.originX, position.originY)
                sr.color = renderedShape.fillColor
                sr.fillRectanglePolygon(rectTempPolygon)
            } else if (renderedShape.fillColor != null) {
                rectTempPolygon.setRectangleShapeAround00(rectangleShape.width, rectangleShape.height)
                rectTempPolygon.polygon.rotation = rotationDegrees
                rectTempPolygon.polygon.setPosition(position.originX, position.originY)
                sr.color = renderedShape.fillColor
                sr.fillRectanglePolygon(rectTempPolygon)
            }
        }
    }

    private fun drawPolygon(triangulatedVisualPolygon: TriangulatedVisualPolygonComponent, position: OriginPositionComponent,
                            renderedShape: VisualShapeComponent, rotationDegrees: Float, borderThickness: Float?) {
        if (renderedShape.fillColor != null) {
            triangulatedVisualPolygon.setRotationDegrees(rotationDegrees)
            val colorOnFullArea: Color
            val colorOnInnerArea: Color?
            if (borderThickness != null) {
                colorOnFullArea = renderedShape.borderColor!!
                colorOnInnerArea = renderedShape.fillColor!!
            } else {
                colorOnFullArea = renderedShape.fillColor!!
                colorOnInnerArea = null
            }

            sr.color = colorOnFullArea
            val triangleVertices = triangulatedVisualPolygon.getTriangleVertices()
            sr.triangles(position.vector, triangleVertices)

            if (colorOnInnerArea != null) {
                sr.color = colorOnInnerArea
                val innerTriangleVertices = triangulatedVisualPolygon.getInnerTriangleVertices()!!
                sr.triangles(position.vector, innerTriangleVertices)
            }
        }
    }

}
