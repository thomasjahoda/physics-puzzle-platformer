package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdx.graphics.mulExceptAlpha
import com.jafleck.extensions.libgdx.physics.box2d.maskAll
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.BodyComponent
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.entities.PlayerComponent
import com.jafleck.game.components.logic.GadgetHolderComponent
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
import com.jafleck.game.families.PhysicalShapedEntity
import com.jafleck.game.gadgets.GadgetSelector
import com.jafleck.game.gadgets.addGadgetHolder
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.libgdx.maps.getNullableStringProperty
import com.jafleck.game.util.libgdx.maps.preset
import com.jafleck.game.util.libgdx.maps.verifyNameSetIfCustomized
import ktx.ashley.allOf
import ktx.box2d.filter
import org.koin.dsl.bind
import org.koin.dsl.module

inline class PlayerEntity(val entity: Entity) {

    companion object {
        val family: Family = allOf(
            PlayerComponent::class
        ).get()
    }

    val position
        get() = entity[OriginPositionComponent]
    val player
        get() = entity[PlayerComponent]
    val body
        get() = entity[BodyComponent]
    val gadgetHolder
        get() = entity[GadgetHolderComponent]

    fun asPhysicalShapedEntity() = PhysicalShapedEntity(entity)
}

class PlayerEntityCreator(
    private val engine: Engine,
    private val genericEntityCustomizationLoader: GenericEntityCustomizationLoader,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val visualShapeCreator: VisualShapeCreator,
    private val genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer,
    private val gadgetSelector: GadgetSelector
) : MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityTypeConfig(
            rotates = true,
            moves = true
        )
    }

    override val type: String
        get() = "Player"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = playerPresets.getPresetOrDefault(mapObject.preset)
        val genericConfig = preset.genericConfig.combine(genericEntityCustomizationLoader.load(mapObject))
        val playerEntityConfig = preset.customEntityConfig.combine(PlayerConfig.fromMapObject(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericConfig, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createDynamicBody(this) {
                filter {
                    categoryBits = CollisionEntityCategory.player
                    maskAll()
                }
                apply(genericConfig, genericPhysicsBodyCustomizer)
            }
            add(visualShapeCreator.createVisualShape(genericConfig))
            addGadgetHolder(playerEntityConfig.initialGadget, gadgetSelector)
            add(PlayerComponent())
            engine.addEntity(this)
        }
    }
}


internal val playerPresets = listOf(
    Preset(genericConfig = GenericEntityConfig(
        borderColor = Color.BLACK, borderThickness = 0.04f,
        fillColor = Color.FIREBRICK.cpy().mulExceptAlpha(0.9f),
        density = 4f,
        friction = 0.2f,
        angularDamping = 0.3f
    ), customEntityConfig = PlayerConfig(
        initialGadget = null
    ))
).asMap()

internal data class PlayerConfig(
    var initialGadget: String?
) {

    fun combine(other: PlayerConfig): PlayerConfig {
        return copy().apply(other)
    }

    fun apply(other: PlayerConfig): PlayerConfig {
        withItIfNotNull(other.initialGadget) { initialGadget = it }
        return this
    }

    companion object {
        fun fromMapObject(mapObject: MapObject): PlayerConfig {
            return PlayerConfig(
                mapObject.initialGadget
            )
        }
    }
}

internal val MapObject.initialGadget
    get() = properties.getNullableStringProperty("initialGadget").also { verifyNameSetIfCustomized(it) }

val playerModule = module {
    single { PlayerEntityCreator(get(), get(), get(), get(), get(), get(), get()) } bind MapEntityLoader::class
}
