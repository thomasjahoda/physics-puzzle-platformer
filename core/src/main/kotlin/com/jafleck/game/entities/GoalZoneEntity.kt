package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdx.physics.box2d.maskAll
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.entities.GoalZoneComponent
import com.jafleck.game.components.zone.EntityCollisionTrackingZoneComponent
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
import com.jafleck.game.families.ShapedEntity
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.util.libgdx.map.preset
import ktx.box2d.filter
import ktx.graphics.copy
import org.koin.dsl.module

inline class GoalZoneEntity(val entity: Entity) {

    fun asShapedEntity() = ShapedEntity(entity)

    val position
        get() = entity[OriginPositionComponent]
    val goalZone
        get() = entity[GoalZoneComponent]
}

class GoalZoneEntityCreator(
    private val engine: Engine,
    private val genericEntityCustomizationLoader: GenericEntityCustomizationLoader,
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val visualShapeCreator: VisualShapeCreator,
    private val genericPhysicsBodyCustomizer: GenericPhysicsBodyCustomizer
) : MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityConfig(
            rotates = false,
            moves = false
        )
    }

    override val type: String
        get() = "GoalZone"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = goalZonePresets.getPresetOrDefault(mapObject.preset)
        val genericCustomization = preset.genericCustomization.combine(genericEntityCustomizationLoader.load(mapObject))
        return engine.createEntity().apply {
            loadGeneralComponentsFrom(mapObject, ENTITY_CONFIG, genericCustomization, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createStaticBody(this) {
                filter {
                    categoryBits = CollisionEntityCategory.default
                    maskBits = CollisionEntityCategory.player
                }
                apply(genericCustomization, genericPhysicsBodyCustomizer)
                isSensor = true
            }
            add(visualShapeCreator.createVisualShape(genericCustomization))
            add(EntityCollisionTrackingZoneComponent())
            add(GoalZoneComponent())
            engine.addEntity(this)
        }
    }

}

val goalZonePresets = listOf(
    Preset(genericCustomization = GenericEntityCustomization(
        fillColor = Color.GOLD.copy(alpha = 0.4f)
    ))
).asMap()

val goalZoneModule = module {
    single { GoalZoneEntityCreator(get(), get(), get(), get(), get(), get()) }
}
