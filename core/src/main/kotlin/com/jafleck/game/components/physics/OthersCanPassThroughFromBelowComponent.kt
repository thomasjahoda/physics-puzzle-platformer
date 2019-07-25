package com.jafleck.game.components.physics

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class OthersCanPassThroughFromBelowComponent : Component {

    companion object : ComponentMapperAccessor<OthersCanPassThroughFromBelowComponent>(OthersCanPassThroughFromBelowComponent::class)
}
