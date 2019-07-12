package com.jafleck.game.components.logic

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.gadgets.Gadget

data class SelectedGadgetComponent(
    var value: Gadget
) : Component {

    companion object : ComponentMapperAccessor<SelectedGadgetComponent>(SelectedGadgetComponent::class)
}
