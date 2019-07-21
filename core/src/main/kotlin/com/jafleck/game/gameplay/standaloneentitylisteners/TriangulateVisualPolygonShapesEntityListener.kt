package com.jafleck.game.gameplay.standaloneentitylisteners

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.EarClippingTriangulator
import com.badlogic.gdx.math.Polygon
import com.jafleck.extensions.jts.to2dVerticesArray
import com.jafleck.extensions.jts.toJtsCoordinates
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.has
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.visual.TriangulatedVisualPolygonComponent
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.util.ashley.EntityFamilyListener
import com.jafleck.game.util.math.triangulate
import ktx.ashley.allOf
import ktx.ashley.remove
import org.locationtech.jts.geom.GeometryFactory


class TriangulateVisualPolygonShapesEntityListener : EntityFamilyListener {

    private val polygonTriangulator = EarClippingTriangulator()

    override val family: Family
        get() = allOf(
            VisualShapeComponent::class,
            PolygonShapeComponent::class
        ).get()

    override fun entityAdded(entity: Entity) {
        if (entity.has(TriangulatedVisualPolygonComponent).not()) {
            val polygonShapeComponent = entity[PolygonShapeComponent]
            val polygonVertices = polygonShapeComponent.vertices
            val unrotatedTriangleVertices = triangulate(polygonVertices, polygonTriangulator)
            val trianglePolygon = Polygon(unrotatedTriangleVertices)

            val visualShapeComponent = entity[VisualShapeComponent]
            val innerTrianglePolygon: Polygon?
            if (visualShapeComponent.borderThickness != null) {
                val polygonCoordinates = polygonVertices.toJtsCoordinates()
                val innerPolygonalGeometry = GeometryFactory().createPolygon(polygonCoordinates)
                    .buffer(visualShapeComponent.borderThickness!!.toDouble() * -1)
                if (innerPolygonalGeometry !is org.locationtech.jts.geom.Polygon) {
                    error("While calculating the inner polygon (polygon shape spaced with border thickness) has split up the polygon into multiple polygons. " +
                        "The actual geometry is $innerPolygonalGeometry. " +
                        "It should have been a Polygon instead. Either decrease the border thickness, implement support for multiple polygons as inner polygons. " +
                        "You can also resize your polygon so all edges have more than 2*borderThickness space from every non-adjacent edge.")
                }
                val innerPolygonVertices = innerPolygonalGeometry.coordinates.to2dVerticesArray()
                val unrotatedInnerTriangleVertices = triangulate(innerPolygonVertices, polygonTriangulator)
                innerTrianglePolygon = Polygon(unrotatedInnerTriangleVertices)
            } else {
                innerTrianglePolygon = null
            }

            val triangulatedVisualPolygonComponent = TriangulatedVisualPolygonComponent(trianglePolygon, innerTrianglePolygon)
            if (entity.has(RotationComponent)) {
                triangulatedVisualPolygonComponent.setRotationDegrees(entity[RotationComponent].degrees)
            }
            entity.add(triangulatedVisualPolygonComponent)
        }
    }

    override fun entityRemoved(entity: Entity) {
        if (entity.has(TriangulatedVisualPolygonComponent)) {
            entity.remove<TriangulatedVisualPolygonComponent>()
        }
    }
}
