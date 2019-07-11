package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.PlatformComponent
import com.jafleck.game.components.VisualShapeComponent
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.maploading.*
import com.jafleck.game.maploading.MapEntityLoader
import org.koin.dsl.module

inline class PlatformEntity(val entity: Entity) {

    companion object {
        const val FRICTION = 0.2f
    }

    val position
        get() = entity[OriginPositionComponent]
    val platform
        get() = entity[PlatformComponent]
}

class PlatformEntityCreator(
    private val engine: Engine,
    private val world: World,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val customizeVisualShapeLoader: CustomizeVisualShapeLoader
) : MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityConfig(
            rotates = true,
            moves = false
        )
    }

    override val type: String
        get() = "Platform"

    override fun loadEntity(mapObject: MapObject): Entity {
        return engine.createEntity().apply {
            loadFrom(mapObject, ENTITY_CONFIG, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createStaticBody(this) {
                friction = PlayerEntity.FRICTION
            }
            add(VisualShapeComponent(
                borderColor = Color.PURPLE, borderThickness = 0.1f,
                fillColor = Color.WHITE.cpy().mul(0.9f))
                .customizeBy(mapObject, customizeVisualShapeLoader))
            add(PlatformComponent())
            engine.addEntity(this)
        }
    }

}

val platformModule = module {
    single { PlatformEntityCreator(get(), get(), get(), get(), get()) }
}
