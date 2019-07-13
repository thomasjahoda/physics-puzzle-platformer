package com.jafleck.game.entities.maploading

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Ellipse
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.map.id
import com.jafleck.extensions.libgdx.map.rotationDegrees
import com.jafleck.game.components.basic.MapObjectComponent
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.basic.VelocityComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.EllipseShapeComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.entities.customizations.GenericEntityCustomization
import com.jafleck.game.maploading.scaleFromMapToWorld

class MapObjectFormExtractor {

    fun extractShapeAndPositionComponents(mapObject: MapObject,
                                          config: GenericEntityConfig,
                                          customization: GenericEntityCustomization): List<Component> {
        val components = ArrayList<Component>(3)

        val rotationDegrees = extractRotation(mapObject, config.rotates, components)
        extractAnyShapeAndPosition(mapObject, rotationDegrees, components)
        extractInitialVelocity(mapObject, customization, config.moves, components)
        if (config.trackMapObject) components.add(MapObjectComponent(mapObject))

        return components
    }

    private fun extractAnyShapeAndPosition(mapObject: MapObject, rotationDegrees: Float, components: ArrayList<Component>) {
        when (mapObject) {
            is RectangleMapObject -> extractRectangleShapeAndPosition(mapObject, rotationDegrees, components)
            is EllipseMapObject -> extractEllipseShapeAndPosition(mapObject, rotationDegrees, components)
            is PolygonMapObject -> extractPolygonShapeAndPosition(mapObject, rotationDegrees, components)
            else -> error("Unknown shape of object ${mapObject.id}: ${mapObject.javaClass}")
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

    private fun extractEllipseShapeAndPosition(mapObject: EllipseMapObject, rotationDegrees: Float, components: ArrayList<Component>) {
        val ellipse = mapObject.ellipse
        require(rotationDegrees == 0f) { "Rotated ellipses are not supported yet (${mapObject.id})" } // TODO support rotated ellipses
        val worldOriginPosition = Vector2(ellipse.x + ellipse.width / 2, ellipse.y + ellipse.height / 2).scaleFromMapToWorld()
        val worldRectangleSize = Vector2(ellipse.width.scaleFromMapToWorld(), ellipse.height.scaleFromMapToWorld())
        components.add(OriginPositionComponent(worldOriginPosition))
        if (ellipse.isCircle()) {
            components.add(CircleShapeComponent(worldRectangleSize.x / 2))
        } else {
            components.add(EllipseShapeComponent(worldRectangleSize))
        }
    }

    private fun Ellipse.isCircle() = this.width == this.height

    private fun extractPolygonShapeAndPosition(mapObject: PolygonMapObject, rotationDegrees: Float, components: ArrayList<Component>) {
        val mapPolygon = mapObject.polygon

        if (rotationDegrees == 0f) {
            val (absoluteOriginPosition, worldVertices) = determineAbsolutePolygonOriginAndPolygonVerticesRelativeToOrigin(mapPolygon)

            components.add(OriginPositionComponent(absoluteOriginPosition.scaleFromMapToWorld()))
            components.add(PolygonShapeComponent(worldVertices))
        } else {
            // rotate around specified rotation point (x,y in map object) of Tiled (just like Tiled does when displaying the polygon)
            mapPolygon.rotation = rotationDegrees
            val (absoluteOriginPosition, worldVertices) = determineAbsolutePolygonOriginAndPolygonVerticesRelativeToOrigin(mapPolygon)

            // rotate polygon back to 0°, but now with the actual origin of the polygon -> results in the actual original (unrotated) polygon as designed in the map before the stupid rotation transformation by Tiled
            val worldPolygon = Polygon(worldVertices)
            worldPolygon.rotation = -rotationDegrees
            val originalUnrotatedWorldVertices = worldPolygon.transformedVertices
            components.add(OriginPositionComponent(absoluteOriginPosition.scaleFromMapToWorld()))
            components.add(PolygonShapeComponent(originalUnrotatedWorldVertices))
        }
    }

    private fun determineAbsolutePolygonOriginAndPolygonVerticesRelativeToOrigin(mapPolygon: Polygon): Pair<Vector2, FloatArray> {
        val absolutePolygonBoundingRectangle = mapPolygon.boundingRectangle
        val absoluteOriginPosition = absolutePolygonBoundingRectangle.getCenter(Vector2())
        // move vertices of polygon to be relative to the polygon origin position
        val translationToPolygonOrigin = absoluteOriginPosition.cpy().scl(-1f)
        val absoluteMapVertices = mapPolygon.transformedVertices
        val worldVertices = absoluteMapVertices.clone()
        translateVerticesAndConvertToWorldScale(worldVertices, translationToPolygonOrigin)
        return Pair(absoluteOriginPosition, worldVertices)
    }

    private fun translateVerticesAndConvertToWorldScale(mapVertices: FloatArray, verticesPositionShift: Vector2) {
        for (i in 0 until mapVertices.size step 2) {
            mapVertices[i] = (mapVertices[i] + verticesPositionShift.x).scaleFromMapToWorld()
            mapVertices[i + 1] = (mapVertices[i + 1] + verticesPositionShift.y).scaleFromMapToWorld()
        }
    }

    private fun extractInitialVelocity(mapObject: MapObject, customization: GenericEntityCustomization, moves: Boolean, components: ArrayList<Component>) {
        if (!moves) {
            require(customization.initialVelocity == null) { "Object with name ${mapObject.name} has initial velocity but it is not supported for this type because moves==false" }
        } else {
            val velocity = customization.initialVelocity ?: Vector2(0f, 0f)
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
)

/**
 * Loads:
 * - position
 * - shape
 * - rotation
 * - velocity
 * - component for linking map object
 */
fun Entity.loadGeneralComponentsFrom(mapObject: MapObject,
                                     config: GenericEntityConfig,
                                     customization: GenericEntityCustomization,
                                     mapObjectFormExtractor: MapObjectFormExtractor) {
    mapObjectFormExtractor.extractShapeAndPositionComponents(mapObject, config, customization)
        .forEach { this@loadGeneralComponentsFrom.add(it) }
}
