package com.jafleck.game.components.physics

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class PassesThroughFromBelowComponent(
    var passingThroughCount: Int = 1
) : Component {

    companion object : ComponentMapperAccessor<PassesThroughFromBelowComponent>(PassesThroughFromBelowComponent::class)
}
