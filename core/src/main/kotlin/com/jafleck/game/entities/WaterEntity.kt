package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.entities.WaterComponent
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
import com.jafleck.game.util.libgdx.maps.preset
import ktx.box2d.filter
import ktx.graphics.copy
import org.koin.dsl.bind
import org.koin.dsl.module

inline class WaterEntity(val entity: Entity) {

    fun asShapedEntity() = ShapedEntity(entity)

    val position
        get() = entity[OriginPositionComponent]
    val water
        get() = entity[WaterComponent]
}

class WaterEntityCreator(
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
        get() = "Water"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = waterPresets.getPresetOrDefault(mapObject.preset)
        val genericConfig = preset.genericConfig.combine(genericEntityCustomizationLoader.load(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericConfig, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createStaticBody(this) {
                filter {
                    categoryBits = CollisionEntityCategory.environment
                }
                apply(genericConfig, genericPhysicsBodyCustomizer)
                isSensor = true
            }
            add(visualShapeCreator.createVisualShape(genericConfig))
            add(WaterComponent())
            engine.addEntity(this)
        }
    }

}

internal val waterPresets = listOf(
    genericPreset(genericConfig = GenericEntityConfig(
        fillColor = Color.BLUE.copy(alpha = 0.6f)
    ))
).asMap()

val waterModule = module {
    single { WaterEntityCreator(get(), get(), get(), get(), get(), get()) } bind MapEntityLoader::class
}
