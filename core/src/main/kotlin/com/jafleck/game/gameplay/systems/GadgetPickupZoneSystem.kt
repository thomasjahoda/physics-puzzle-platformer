package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.entities.GadgetPickupZoneComponent
import com.jafleck.game.components.logic.GadgetHolderComponent
import com.jafleck.game.entities.GadgetPickupZoneEntity
import com.jafleck.game.gadgets.GadgetHolderEntity
import com.jafleck.game.gadgets.GadgetSelector
import com.jafleck.game.util.ashley.getDebugDump
import com.jafleck.game.util.logger
import ktx.ashley.allOf


class GadgetPickupZoneSystem(
    private val gadgetSelector: GadgetSelector
) : IteratingSystem(allOf(GadgetPickupZoneComponent::class).get()) {

    private val logger = logger(this::class)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val typedEntity = GadgetPickupZoneEntity(entity)
        val entitiesWithinZone = typedEntity.entityCollisionTrackingZone.entitiesWithinZone
        if (entitiesWithinZone.isNotEmpty()) {
            entitiesWithinZone.forEach { entityWithinZone ->
                val gadgetHolderComponent = entityWithinZone[GadgetHolderComponent]
                if (gadgetHolderComponent.selectedGadget != typedEntity.gadgetPickupZone.gadget) {
                    logger.debug { "Entity ${entityWithinZone.getDebugDump()} is in gadget pickup zone and will get gadget ${typedEntity.gadgetPickupZone.gadget}" }
                    gadgetSelector.selectGadget(GadgetHolderEntity(entityWithinZone), typedEntity.gadgetPickupZone.gadget)
                }
            }
        }
    }
}
