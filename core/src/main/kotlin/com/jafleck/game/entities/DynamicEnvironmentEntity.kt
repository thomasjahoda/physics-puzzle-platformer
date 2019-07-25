package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdx.graphics.mulExceptAlpha
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.entities.DynamicEnvironmentComponent
import com.jafleck.game.entities.config.CommonPhysicsConfigs
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
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.libgdx.maps.preset
import ktx.box2d.filter
import org.koin.dsl.bind
import org.koin.dsl.module

inline class DynamicEnvironmentEntity(val entity: Entity) {

    val position
        get() = entity[OriginPositionComponent]
    val dynamicEnvironment
        get() = entity[DynamicEnvironmentComponent]
}

class DynamicEnvironmentEntityCreator(
    private val engine: Engine,
    private val genericEntityCustomizationLoader: GenericEntityCustomizationLoader,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val visualShapeCreator: VisualShapeCreator,
    private val genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer
) : MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityTypeConfig(
            rotates = true,
            moves = true
        )
    }

    override val type: String
        get() = "DynamicEnvironment"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = dynamicEnvironmentPresets.getPresetOrDefault(mapObject.preset)
        val genericConfig = preset.genericConfig.combine(genericEntityCustomizationLoader.load(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericConfig, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createDynamicBody(this) {
                filter {
                    categoryBits = CollisionEntityCategory.environment
                }
                apply(genericConfig, genericPhysicsBodyCustomizer)
            }
            add(visualShapeCreator.createVisualShape(genericConfig))
            add(DynamicEnvironmentComponent())
            engine.addEntity(this)
        }
    }

}

internal val dynamicEnvironmentPresets = listOf(
    genericPreset(genericConfig = GenericEntityConfig(
        density = 2.5f,
//        borderColor = Color.DARK_GRAY, borderThickness = 0.1f,
        fillColor = Color.RED.cpy().mulExceptAlpha(1f)
    )),
    genericPreset("Bouncy",
        genericConfig = GenericEntityConfig(
            density = 2.5f,
            borderColor = Color.GREEN, borderThickness = 0.1f,
            fillColor = Color.WHITE.cpy().mulExceptAlpha(0.9f)
        ).apply(CommonPhysicsConfigs.BOUNCY)
    )
).asMap()

val dynamicEnvironmentModule = module {
    single { DynamicEnvironmentEntityCreator(get(), get(), get(), get(), get(), get()) } bind MapEntityLoader::class
}
