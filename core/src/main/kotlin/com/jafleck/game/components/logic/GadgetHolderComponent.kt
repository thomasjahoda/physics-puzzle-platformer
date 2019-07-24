package com.jafleck.game.components.logic

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.gadgets.Gadget

data class GadgetHolderComponent(
    var selectedGadget: Gadget?
) : Component {

    companion object : ComponentMapperAccessor<GadgetHolderComponent>(GadgetHolderComponent::class)
}
