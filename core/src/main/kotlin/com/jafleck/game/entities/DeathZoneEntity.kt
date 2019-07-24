package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.math.Rectangle
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.entities.DeathZoneComponent
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.components.zone.EntityCollisionTrackingZoneComponent
import com.jafleck.game.entities.config.GenericEntityConfig
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCustomizer
import com.jafleck.game.entities.creatorutil.VisualShapeCreator
import com.jafleck.game.entities.creatorutil.apply
import com.jafleck.game.entities.maploading.GenericEntityCustomizationLoader
import com.jafleck.game.entities.maploading.GenericEntityTypeConfig
import com.jafleck.game.entities.maploading.MapObjectFormExtractor
import com.jafleck.game.entities.maploading.loadGeneralComponentsFrom
import com.jafleck.game.entities.physics.CollisionEntityCategory
import com.jafleck.game.entities.presets.asMap
import com.jafleck.game.entities.presets.genericPreset
import com.jafleck.game.entities.presets.getPresetOrDefault
import com.jafleck.game.families.ShapedEntity
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.ashley.addIfNotNull
import com.jafleck.game.util.ashley.createPositionAndForm
import com.jafleck.game.util.libgdx.maps.preset
import ktx.box2d.filter
import ktx.graphics.copy
import org.koin.dsl.bind
import org.koin.dsl.module

inline class DeathZoneEntity(val entity: Entity) {

    fun asShapedEntity() = ShapedEntity(entity)

    val visualShape
        get() = entity[VisualShapeComponent]
    val entityCollisionTrackingZone
        get() = entity[EntityCollisionTrackingZoneComponent]
    val deathZone
        get() = entity[DeathZoneComponent]
}

class DeathZoneEntityCreator(
    private val engine: Engine,
    private val genericEntityCustomizationLoader: GenericEntityCustomizationLoader,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val visualShapeCreator: VisualShapeCreator,
    private val genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer
) : MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityTypeConfig(
            rotates = false,
            moves = false
        )
    }

    override val type: String
        get() = "DeathZone"

    fun createInvisibleDeathZone(rectangle: Rectangle): DeathZoneEntity {
        val preset = deathZonePresets.getValue("invisible")
        val genericConfig = preset.genericConfig
        return engine.createEntity().apply {
            createPositionAndForm(rectangle)
            generateNonGeneralComponents(genericConfig)
            engine.addEntity(this)
        }.let { DeathZoneEntity(it) }
    }

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = deathZonePresets.getPresetOrDefault(mapObject.preset)
        val genericConfig = preset.genericConfig.combine(genericEntityCustomizationLoader.load(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericConfig, mapObjectFormExtractor)
            generateNonGeneralComponents(genericConfig)
            engine.addEntity(this)
        }
    }

    private fun Entity.generateNonGeneralComponents(genericConfig: com.jafleck.game.entities.config.GenericEntityConfig) {
        genericPhysicsBodyCreator.createStaticBody(this) {
            filter {
                categoryBits = CollisionEntityCategory.default
                maskBits = CollisionEntityCategory.player
            }
            apply(genericConfig, genericPhysicsBodyCustomizer)
            isSensor = true
        }
        addIfNotNull(visualShapeCreator.createVisualShapeIfColorIsSet(genericConfig))
        add(EntityCollisionTrackingZoneComponent())
        add(DeathZoneComponent())
    }

}

internal val deathZonePresets = listOf(
    genericPreset("invisible"),
    genericPreset("visible", genericConfig = GenericEntityConfig(
        fillColor = Color.RED.copy(alpha = 0.4f)
    ))
).asMap()

val deathZoneModule = module {
    single { DeathZoneEntityCreator(get(), get(), get(), get(), get(), get()) } bind MapEntityLoader::class
}
