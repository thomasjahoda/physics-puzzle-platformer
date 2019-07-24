package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.entities.GoalZoneComponent
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
import com.jafleck.game.util.libgdx.maps.preset
import ktx.box2d.filter
import ktx.graphics.copy
import org.koin.dsl.bind
import org.koin.dsl.module

inline class GoalZoneEntity(val entity: Entity) {

    fun asShapedEntity() = ShapedEntity(entity)

    val visualShape
        get() = entity[VisualShapeComponent]
    val entityCollisionTrackingZone
        get() = entity[EntityCollisionTrackingZoneComponent]
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
        private val ENTITY_CONFIG = GenericEntityTypeConfig(
            rotates = false,
            moves = false
        )
        internal val DEFAULT_START_COLOR = Color.GOLD.copy(alpha = 0.4f)
        internal val END_COLOR = Color.GREEN.copy(alpha = 0.4f)
    }

    override val type: String
        get() = "GoalZone"

    override fun loadEntity(mapObject: MapObject): Entity {
        val preset = goalZonePresets.getPresetOrDefault(mapObject.preset)
        val genericConfig = preset.genericConfig.combine(genericEntityCustomizationLoader.load(mapObject))
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
            add(GoalZoneComponent(startColor = visualShape.fillColor!!.cpy(), endColor = END_COLOR))
            engine.addEntity(this)
        }
    }

}

internal val goalZonePresets = listOf(
    genericPreset(genericConfig = GenericEntityConfig(
        fillColor = GoalZoneEntityCreator.DEFAULT_START_COLOR
        // alpha and borders currently do not work as expected currently, therefore disabled
//        borderColor = GoalZoneEntityCreator.DEFAULT_START_COLOR.copy(alpha = 1f),
//        borderThickness = 0.1f
    ))
).asMap()

val goalZoneModule = module {
    single { GoalZoneEntityCreator(get(), get(), get(), get(), get(), get()) } bind MapEntityLoader::class
}
