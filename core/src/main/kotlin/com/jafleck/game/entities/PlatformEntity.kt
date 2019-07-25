package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdx.graphics.mulExceptAlpha
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.entities.PlatformComponent
import com.jafleck.game.components.physics.OthersCanPassThroughFromBelowComponent
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
import com.jafleck.game.entities.presets.Preset
import com.jafleck.game.entities.presets.asMap
import com.jafleck.game.entities.presets.getPresetOrDefault
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.libgdx.maps.preset
import ktx.box2d.filter
import org.koin.dsl.bind
import org.koin.dsl.module

inline class PlatformEntity(val entity: Entity) {

    val position
        get() = entity[OriginPositionComponent]
    val platform
        get() = entity[PlatformComponent]
}

class PlatformEntityCreator(
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
            moves = false
        )
    }

    override val type: String
        get() = "Platform"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = platformPresets.getPresetOrDefault(mapObject.preset)
        val genericConfig = preset.genericConfig.combine(genericEntityCustomizationLoader.load(mapObject))
        val platformConfig = preset.customEntityConfig
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericConfig, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createStaticBody(this) {
                filter {
                    categoryBits = CollisionEntityCategory.environment
                }
                apply(genericConfig, genericPhysicsBodyCustomizer)
            }
            add(visualShapeCreator.createVisualShape(genericConfig))
            add(PlatformComponent())
            if (platformConfig.passThroughFromBelow) {
                add(OthersCanPassThroughFromBelowComponent())
            }
            engine.addEntity(this)
        }
    }

}

internal val platformPresets = listOf(
    Preset(
        genericConfig = GenericEntityConfig(
            borderColor = Color.PURPLE, borderThickness = 0.1f,
            fillColor = Color.WHITE.cpy().mulExceptAlpha(0.9f)
        ),
        customEntityConfig = PlatformConfig(
            passThroughFromBelow = false
        )),
    Preset("Trampoline",
        genericConfig = GenericEntityConfig(
            borderColor = Color.GREEN, borderThickness = 0.1f,
            fillColor = Color.WHITE.cpy().mulExceptAlpha(0.9f)
        ).apply(CommonPhysicsConfigs.BOUNCY),
        customEntityConfig = PlatformConfig(
            passThroughFromBelow = false
        )),
    Preset("PassThroughFromBelow",
        genericConfig = GenericEntityConfig(
            borderColor = Color.CORAL, borderThickness = 0.1f,
            fillColor = Color.WHITE.cpy().mulExceptAlpha(0.9f)
        ),
        customEntityConfig = PlatformConfig(
            passThroughFromBelow = true
        ))
).asMap()

internal data class PlatformConfig(
    var passThroughFromBelow: Boolean = false
)

val platformModule = module {
    single { PlatformEntityCreator(get(), get(), get(), get(), get(), get()) } bind MapEntityLoader::class
}
