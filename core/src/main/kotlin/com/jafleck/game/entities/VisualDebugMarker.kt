package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.entities.VisualDebugMarkerComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCustomizer
import com.jafleck.game.entities.creatorutil.VisualShapeCreator
import com.jafleck.game.entities.maploading.GenericEntityCustomizationLoader
import com.jafleck.game.entities.maploading.MapObjectFormExtractor
import com.jafleck.game.families.ShapedEntity
import org.koin.dsl.module

inline class VisualDebugMarkerEntity(val entity: Entity) {

    fun asShapedEntity() = ShapedEntity(entity)

    val position
        get() = entity[OriginPositionComponent]
}

class VisualDebugMarkerEntityCreator(
    private val engine: Engine,
    private val genericEntityCustomizationLoader: GenericEntityCustomizationLoader,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val visualShapeCreator: VisualShapeCreator,
    private val genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer
) {
    companion object {
        lateinit var instance: VisualDebugMarkerEntityCreator
    }

    init {
        instance = this
    }

    fun createMarker(originPosition: Vector2, color: Color, text: String? = null): Entity {
        return engine.createEntity().apply {
            add(OriginPositionComponent(originPosition))
            add(CircleShapeComponent(0.1f))
            add(VisualShapeComponent(fillColor = color))
            add(VisualDebugMarkerComponent(text))
            engine.addEntity(this)
        }
    }

    fun createMarkerFromCurrentEntity(entity: Entity, color: Color, text: String? = null): Entity {
        return engine.createEntity().apply {
            withItIfNotNull(entity.getOrNull(OriginPositionComponent)) {
                add(OriginPositionComponent(it.vector.cpy()))
            }
            withItIfNotNull(entity.getOrNull(RotationComponent)) {
                add(RotationComponent(it.radians))
            }

            withItIfNotNull(entity.getOrNull(RectangleShapeComponent)) {
                add(RectangleShapeComponent(it.vector.cpy()))
            }
            withItIfNotNull(entity.getOrNull(CircleShapeComponent)) {
                add(CircleShapeComponent(it.radius))
            }
            withItIfNotNull(entity.getOrNull(PolygonShapeComponent)) {
                add(PolygonShapeComponent(it.vertices.clone()))
            }

            add(VisualShapeComponent(fillColor = color))
            add(VisualDebugMarkerComponent(text))
            engine.addEntity(this)
        }
    }

}

val visualDebugMarkerModule = module {
    single { VisualDebugMarkerEntityCreator(get(), get(), get(), get(), get(), get()) }
}
