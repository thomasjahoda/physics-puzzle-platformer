package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdx.graphics.mulExceptAlpha
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.entities.PlatformComponent
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCustomizer
import com.jafleck.game.entities.creatorutil.VisualShapeCreator
import com.jafleck.game.entities.creatorutil.apply
import com.jafleck.game.entities.customizations.CommonPhysicsCustomizations
import com.jafleck.game.entities.customizations.GenericEntityCustomization
import com.jafleck.game.entities.maploading.GenericEntityConfig
import com.jafleck.game.entities.maploading.GenericEntityCustomizationLoader
import com.jafleck.game.entities.maploading.MapObjectFormExtractor
import com.jafleck.game.entities.maploading.loadGeneralComponentsFrom
import com.jafleck.game.entities.physics.CollisionEntityCategory
import com.jafleck.game.entities.presets.Preset
import com.jafleck.game.entities.presets.asMap
import com.jafleck.game.entities.presets.getPresetOrDefault
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.libgdx.maps.preset
import ktx.box2d.filter
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
        private val ENTITY_CONFIG = GenericEntityConfig(
            rotates = true,
            moves = false
        )
    }

    override val type: String
        get() = "Platform"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = platformPresets.getPresetOrDefault(mapObject.preset)
        val genericCustomization = preset.genericCustomization.combine(genericEntityCustomizationLoader.load(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericCustomization, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createStaticBody(this) {
                filter {
                    categoryBits = CollisionEntityCategory.environment
                }
                apply(genericCustomization, genericPhysicsBodyCustomizer)
            }
            add(visualShapeCreator.createVisualShape(genericCustomization))
            add(PlatformComponent())
            engine.addEntity(this)
        }
    }

}

val platformPresets = listOf(
    Preset(genericCustomization = GenericEntityCustomization(
        borderColor = Color.PURPLE, borderThickness = 0.1f,
        fillColor = Color.WHITE.cpy().mulExceptAlpha(0.9f)
    )),
    Preset("Trampoline",
        genericCustomization = GenericEntityCustomization(
            borderColor = Color.GREEN, borderThickness = 0.1f,
            fillColor = Color.WHITE.cpy().mulExceptAlpha(0.9f)
        ).apply(CommonPhysicsCustomizations.BOUNCY)
    )
).asMap()

val platformModule = module {
    single { PlatformEntityCreator(get(), get(), get(), get(), get(), get()) }
}
