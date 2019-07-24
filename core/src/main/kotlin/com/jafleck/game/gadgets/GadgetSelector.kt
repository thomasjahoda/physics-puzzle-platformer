package com.jafleck.game.gadgets

import com.badlogic.ashley.core.Entity
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.logic.GadgetHolderComponent


inline class GadgetHolderEntity(val entity: Entity) {

    val gadgetHolder
        get() = entity[GadgetHolderComponent]
}

class GadgetSelector(
    private val gadgetLocator: GadgetLocator
) {

    fun selectGadget(entity: GadgetHolderEntity, gadget: Gadget) {
        withItIfNotNull(entity.gadgetHolder.selectedGadget) {
            entity.gadgetHolder.selectedGadget = null
            it.unselected(entity.entity)
        }
        entity.gadgetHolder.selectedGadget = gadget
    }

    fun selectGadget(entity: GadgetHolderEntity, gadgetName: String) {
        selectGadget(entity, gadgetLocator.getGadget(gadgetName))
    }
}

fun Entity.addGadgetHolder(gadgetName: String?, gadgetSelector: GadgetSelector) {
    val gadgetHolder = GadgetHolderComponent(null)
    add(gadgetHolder)
    if (gadgetName != null) {
        gadgetSelector.selectGadget(GadgetHolderEntity(this), gadgetName)
    }
}
