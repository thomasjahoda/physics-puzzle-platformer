package com.jafleck.game.entities.maploading

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.map.id
import com.jafleck.extensions.libgdx.map.rotationDegrees
import com.jafleck.game.components.MapObjectComponent
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.RotationComponent
import com.jafleck.game.components.VelocityComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.maploading.scaleFromMapToWorld
import com.jafleck.game.util.libgdx.map.velocity
import com.jafleck.game.util.logger

class MapObjectFormExtractor {

    private val logger = logger(this::class)

    fun extractShapeAndPositionComponents(mapObject: MapObject,
                                          config: GenericEntityConfig): List<Component> {
        logger.debug { "Parsing map object ${mapObject.id}" }
        val components = ArrayList<Component>(3)

        val rotationDegrees = extractRotation(mapObject, config.rotates, components)
        extractAnyShape(mapObject, rotationDegrees, components)
        extractVelocity(mapObject, config.moves, components)
        if (config.trackMapObject) components.add(MapObjectComponent(mapObject))

        return components
    }

    private fun extractAnyShape(mapObject: MapObject, rotationDegrees: Float, components: ArrayList<Component>) {
        when (mapObject) {
            is RectangleMapObject -> extractRectangleShapeAndPosition(mapObject, rotationDegrees, components)
            is CircleMapObject -> extractCircleShapeAndPosition(mapObject, rotationDegrees, components)
            is EllipseMapObject -> extractEllipseShapeAndPosition(mapObject, rotationDegrees, components)
            else -> error("Unknown shape of object ${mapObject.properties["id"]}: ${mapObject.javaClass}")
        }
    }

    private fun extractRectangleShapeAndPosition(mapObject: RectangleMapObject, rotationDegrees: Float, components: ArrayList<Component>) {
        val rectangleShape = mapObject.rectangle.getSize(Vector2())
        components.add(RectangleShapeComponent(rectangleShape.cpy().scaleFromMapToWorld()))

        val bottomLeftCornerPosition = mapObject.rectangle.getPosition(Vector2())
        if (rotationDegrees == 0f) {
            components.add(OriginPositionComponent(bottomLeftCornerPosition.add(rectangleShape.x / 2f, rectangleShape.y / 2f).scaleFromMapToWorld()))
        } else {
            // rectangles in tiled have a position pointing to the top-left corner and the rotation rotates around the top-left corner
            val topLeftCornerPosition = bottomLeftCornerPosition.cpy().add(0f, rectangleShape.y)
            val originPosition = bottomLeftCornerPosition.cpy().add(rectangleShape.x / 2f, rectangleShape.y / 2f)
                .rotateAround(topLeftCornerPosition, rotationDegrees)
            components.add(OriginPositionComponent(originPosition.scaleFromMapToWorld()))
        }
    }

    private fun extractCircleShapeAndPosition(mapObject: CircleMapObject, rotationDegrees: Float, components: ArrayList<Component>) {
        val circle = mapObject.circle
        require(rotationDegrees == 0f) { "Rotated circles are not supported (${mapObject.id})"}
        components.add(CircleShapeComponent(circle.radius))
        components.add(OriginPositionComponent(Vector2(circle.x, circle.y).scaleFromMapToWorld()))
    }

    private fun extractEllipseShapeAndPosition(mapObject: EllipseMapObject, rotationDegrees: Float, components: ArrayList<Component>) {
        val ellipse = mapObject.ellipse
        require(rotationDegrees == 0f) { "Rotated ellipses are not supported yet (${mapObject.id})"} // TODO support rotated ellipses
        require(ellipse.width == ellipse.height) { "Only circle-formed ellipses supported currently" }
        components.add(CircleShapeComponent((ellipse.width / 2).scaleFromMapToWorld()))
        components.add(OriginPositionComponent(Vector2(ellipse.x, ellipse.y).scaleFromMapToWorld()))
    }

    private fun extractVelocity(mapObject: MapObject, moves: Boolean, components: ArrayList<Component>) {
        if (!moves) {
            require(mapObject.properties["velocity"] == null) { "Object with name ${mapObject.name} has velocity but velocity is not supported for this type because moves==false" }
        } else {
            val velocity = mapObject.velocity ?: Vector2(0f, 0f)
            components.add(VelocityComponent(velocity))
        }
    }

    private fun extractRotation(mapObject: MapObject, rotates: Boolean, components: ArrayList<Component>): Float {
        val rotationDegrees = getRotationDegrees(rotates, mapObject)
        if (rotates) {
            components.add(RotationComponent(MathUtils.degreesToRadians * rotationDegrees))
        }
        return rotationDegrees
    }

    private fun getRotationDegrees(rotates: Boolean, mapObject: MapObject): Float {
        if (!rotates) {
            require(mapObject.rotationDegrees == null) { "Object with name ${mapObject.name} has rotation but rotation is not supported for this type" }
            return 0f
        }

        return mapObject.rotationDegrees ?: 0f
    }
}

class GenericEntityConfig(
    val rotates: Boolean,
    val moves: Boolean,
    val trackMapObject: Boolean = true
    // TODO generate VisualShapeComponent automatically
//    val renderShape: Boolean = true,
//    val defaultFillColor: Color? = null,
//    val defaultFill: Color? = null,
)

fun Entity.loadFrom(mapObject: MapObject,
                    config: GenericEntityConfig,
                    mapObjectFormExtractor: MapObjectFormExtractor) {
    mapObjectFormExtractor.extractShapeAndPositionComponents(mapObject, config)
        .forEach { this@loadFrom.add(it) }
}
