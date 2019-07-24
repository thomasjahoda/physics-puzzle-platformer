package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.GameFonts
import com.jafleck.game.components.entities.GadgetPickupComponent
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.components.visual.VisualTextComponent
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
import com.jafleck.game.entities.presets.Preset
import com.jafleck.game.entities.presets.asMap
import com.jafleck.game.entities.presets.getPresetOrDefault
import com.jafleck.game.families.ShapedEntity
import com.jafleck.game.gadgets.GadgetLocator
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.libgdx.maps.getNullableStringProperty
import com.jafleck.game.util.libgdx.maps.preset
import ktx.box2d.filter
import ktx.graphics.copy
import org.koin.dsl.bind
import org.koin.dsl.module

inline class GadgetPickupEntity(val entity: Entity) {

    fun asShapedEntity() = ShapedEntity(entity)

    val visualShape
        get() = entity[VisualShapeComponent]
    val entityCollisionTrackingZone
        get() = entity[EntityCollisionTrackingZoneComponent]
    val gadgetPickup
        get() = entity[GadgetPickupComponent]
}

class GadgetPickupEntityCreator(
    private val engine: Engine,
    private val genericEntityCustomizationLoader: GenericEntityCustomizationLoader,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val visualShapeCreator: VisualShapeCreator,
    private val genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer,
    private val gadgetLocator: GadgetLocator,
    private val gameFonts: GameFonts
) : MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityTypeConfig(
            rotates = false,
            moves = false
        )
    }

    override val type: String
        get() = "GadgetPickup"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = gadgetPickupPresets.getPresetOrDefault(mapObject.preset)
        val genericConfig = preset.genericConfig.combine(genericEntityCustomizationLoader.load(mapObject))
        val gadgetPickupZoneEntityConfig = preset.customEntityConfig.combine(GadgetPickupConfig.fromMapObject(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericConfig, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createStaticBody(this) {
                filter {
                    categoryBits = CollisionEntityCategory.default
                    maskBits = CollisionEntityCategory.player
                }
                apply(genericConfig, genericPhysicsBodyCustomizer)
                isSensor = true
            }
            val visualShape = visualShapeCreator.createVisualShape(genericConfig)
            add(visualShape)
            add(EntityCollisionTrackingZoneComponent())
            val gadget = gadgetLocator.getGadget(gadgetPickupZoneEntityConfig.gadget
                ?: error("gadget must be configured"))
            add(VisualTextComponent(gadgetPickupZoneEntityConfig.gadget!!, Color.BROWN, gameFonts.`bold 0_5f world size font`))
            add(GadgetPickupComponent(gadget
            ))
            engine.addEntity(this)
        }
    }
}


internal val gadgetPickupPresets = listOf(
    Preset(genericConfig = GenericEntityConfig(
        fillColor = Color.BLUE.copy(alpha = 0.4f)
    ), customEntityConfig = GadgetPickupConfig(
        gadget = null
    ))
).asMap()

internal data class GadgetPickupConfig(
    var gadget: String?
) {

    fun combine(other: GadgetPickupConfig): GadgetPickupConfig {
        return copy().apply(other)
    }

    fun apply(other: GadgetPickupConfig): GadgetPickupConfig {
        withItIfNotNull(other.gadget) { gadget = it }
        return this
    }

    companion object {
        fun fromMapObject(mapObject: MapObject): GadgetPickupConfig {
            return GadgetPickupConfig(
                mapObject.gadget
            )
        }
    }
}

internal val MapObject.gadget
    get() = properties.getNullableStringProperty("gadget")


val gadgetPickupModule = module {
    single { GadgetPickupEntityCreator(get(), get(), get(), get(), get(), get(), get(), get()) } bind MapEntityLoader::class
}
