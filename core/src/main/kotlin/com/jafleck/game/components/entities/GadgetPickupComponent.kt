package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.gadgets.Gadget

class GadgetPickupComponent(
    val gadget: Gadget
) : Component {

    companion object : ComponentMapperAccessor<GadgetPickupComponent>(GadgetPickupComponent::class)
}
