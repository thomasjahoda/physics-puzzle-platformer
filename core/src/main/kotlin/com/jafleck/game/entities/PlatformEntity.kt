package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.components.BodyComponent
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.PlatformComponent
import com.jafleck.game.components.VisualShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.maploading.getRectangleWorldCoordinates
import ktx.box2d.body
import org.koin.dsl.module

inline class PlatformEntity(val entity: Entity) {

    companion object {
        const val FRICTION = 0.2f
    }

    val position
        get() = entity[OriginPositionComponent]
    val size
        get() = entity[RectangleShapeComponent]
    val platform
        get() = entity[PlatformComponent]
}

class PlatformEntityCreator(
    private val engine: Engine,
    private val world: World,
    assetManager: AssetManager,
    screenToWorldScalingPropagator: ScreenToWorldScalingPropagator
) {
    fun createPlatformEntity(
        rectangle: Rectangle
    ): PlatformEntity {
        val entity = engine.createEntity().apply {
            val originPosition = rectangle.getCenter(Vector2())
            add(OriginPositionComponent(originPosition))
            add(RectangleShapeComponent(rectangle.getSize(Vector2())))
            add(VisualShapeComponent(
                borderColor = Color.PURPLE, borderThickness = null,
                fillColor = Color.WHITE.cpy().mul(0.9f)))
            add(BodyComponent(world.body {
                type = BodyDef.BodyType.StaticBody
                box(rectangle.width, rectangle.height) {
                    friction = PlatformEntity.FRICTION
                }
                position.set(originPosition)
            }))
            add(PlatformComponent())
        }
        engine.addEntity(entity)
        return PlatformEntity(entity)
    }
}


class PlatformEntityMapObjectLoader(
    private val platformEntityCreator: PlatformEntityCreator
) : MapEntityLoader {
    override val type: String
        get() = "Platform"

    override fun loadEntity(mapObject: MapObject) {
        require(mapObject is RectangleMapObject)
        platformEntityCreator.createPlatformEntity(getRectangleWorldCoordinates(mapObject))
    }
}

val platformModule = module {
    single { PlatformEntityCreator(get(), get(), get(), get()) }
    single { PlatformEntityMapObjectLoader(get()) }
}
