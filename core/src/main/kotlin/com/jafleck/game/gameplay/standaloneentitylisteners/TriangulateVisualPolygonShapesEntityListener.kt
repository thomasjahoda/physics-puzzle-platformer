package com.jafleck.game.gameplay.standaloneentitylisteners

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.math.EarClippingTriangulator
import com.badlogic.gdx.math.Polygon
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.has
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.visual.TriangulatedVisualPolygonComponent
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.util.listeners.EntityFamilyListener
import com.jafleck.game.util.math.triangulate
import ktx.ashley.allOf
import ktx.ashley.remove


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
            val unrotatedTriangleVertices = triangulate(polygonShapeComponent.vertices, polygonTriangulator)
            val trianglePolygon = Polygon(unrotatedTriangleVertices)
            val triangulatedVisualPolygonComponent = TriangulatedVisualPolygonComponent(trianglePolygon)
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
