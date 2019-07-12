package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class WaterComponent : Component {

    companion object : ComponentMapperAccessor<WaterComponent>(WaterComponent::class)
}
