package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class DynamicEnvironmentComponent : Component {

    companion object : ComponentMapperAccessor<DynamicEnvironmentComponent>(DynamicEnvironmentComponent::class)
}
