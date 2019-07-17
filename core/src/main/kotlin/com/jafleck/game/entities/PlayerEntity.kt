package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.BodyComponent
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.entities.PlayerComponent
import com.jafleck.game.components.logic.SelectedGadgetComponent
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCustomizer
import com.jafleck.game.entities.creatorutil.VisualShapeCreator
import com.jafleck.game.entities.creatorutil.apply
import com.jafleck.game.entities.customizations.GenericEntityCustomization
import com.jafleck.game.entities.maploading.GenericEntityConfig
import com.jafleck.game.entities.maploading.GenericEntityCustomizationLoader
import com.jafleck.game.entities.maploading.MapObjectFormExtractor
import com.jafleck.game.entities.maploading.loadGeneralComponentsFrom
import com.jafleck.game.entities.physics.CollisionEntityCategory
import com.jafleck.game.entities.presets.Preset
import com.jafleck.game.entities.presets.asMap
import com.jafleck.game.entities.presets.getPresetOrDefault
import com.jafleck.game.gadgets.BallThrowerGadget
import com.jafleck.game.gadgets.Gadget
import com.jafleck.game.gadgets.RopeThrowerGadget
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.libgdx.map.preset
import ktx.box2d.filter
import org.koin.dsl.module

inline class PlayerEntity(val entity: Entity) {

    val position
        get() = entity[OriginPositionComponent]
    val player
        get() = entity[PlayerComponent]
    val body
        get() = entity[BodyComponent]
    val selectedGadget
        get() = entity[SelectedGadgetComponent]
}

class PlayerEntityCreator(
    private val engine: Engine,
    private val genericEntityCustomizationLoader: GenericEntityCustomizationLoader,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val visualShapeCreator: VisualShapeCreator,
    private val genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer,
    private val initialGadget: Gadget
) : MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityConfig(
            rotates = true,
            moves = true
        )
    }

    override val type: String
        get() = "Player"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = playerPresets.getPresetOrDefault(mapObject.preset)
        val genericCustomization = preset.genericCustomization.combine(genericEntityCustomizationLoader.load(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericCustomization, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createDynamicBody(this) {
                filter {
                    categoryBits = CollisionEntityCategory.player
                }
                apply(genericCustomization, genericPhysicsBodyCustomizer)
            }
            add(visualShapeCreator.createVisualShape(genericCustomization))
            add(PlayerComponent())
            add(SelectedGadgetComponent(initialGadget))
            engine.addEntity(this)
        }
    }

}


val playerPresets = listOf(
    Preset(genericCustomization = GenericEntityCustomization(
        borderColor = Color.BLACK, borderThickness = 0.04f,
        fillColor = Color.FIREBRICK.cpy().mul(0.9f),
        density = 4f,
        friction = 0.2f,
        angularDamping = 0.3f
    ))
).asMap()

val playerModule = module {
    single { PlayerEntityCreator(get(), get(), get(), get(), get(), get(), get<RopeThrowerGadget>()) }
}
