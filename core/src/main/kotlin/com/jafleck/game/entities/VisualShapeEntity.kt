package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdx.graphics.mulExceptAlpha
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.entities.VisualShapeComponent
import com.jafleck.game.entities.config.GenericEntityConfig
import com.jafleck.game.entities.creatorutil.VisualShapeCreator
import com.jafleck.game.entities.maploading.GenericEntityCustomizationLoader
import com.jafleck.game.entities.maploading.GenericEntityTypeConfig
import com.jafleck.game.entities.maploading.MapObjectFormExtractor
import com.jafleck.game.entities.maploading.loadGeneralComponentsFrom
import com.jafleck.game.entities.presets.asMap
import com.jafleck.game.entities.presets.genericPreset
import com.jafleck.game.entities.presets.getPresetOrDefault
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.libgdx.maps.preset
import org.koin.dsl.bind
import org.koin.dsl.module

inline class VisualShapeEntity(val entity: Entity) {

    val position
        get() = entity[OriginPositionComponent]
    val visualShape
        get() = entity[VisualShapeComponent]
}

class VisualShapeEntityCreator(
    private val engine: Engine,
    private val genericEntityCustomizationLoader: GenericEntityCustomizationLoader,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val visualShapeCreator: VisualShapeCreator
) : MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityTypeConfig(
            rotates = true,
            moves = false
        )
    }

    override val type: String
        get() = "VisualShape"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = visualShapePresets.getPresetOrDefault(mapObject.preset)
        val genericConfig = preset.genericConfig.combine(genericEntityCustomizationLoader.load(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericConfig, mapObjectFormExtractor)
            add(visualShapeCreator.createVisualShape(genericConfig))
            add(VisualShapeComponent())
            engine.addEntity(this)
        }
    }

}

internal val visualShapePresets = listOf(
    genericPreset(genericConfig = GenericEntityConfig(
        borderColor = Color.PURPLE, borderThickness = 0.1f,
        fillColor = Color.WHITE.cpy().mulExceptAlpha(0.9f)
    ))
).asMap()

val visualShapeModule = module {
    single { VisualShapeEntityCreator(get(), get(), get(), get()) } bind MapEntityLoader::class
}
