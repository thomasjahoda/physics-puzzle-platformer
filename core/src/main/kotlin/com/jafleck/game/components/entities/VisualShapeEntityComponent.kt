package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class VisualShapeEntityComponent : Component {

    companion object : ComponentMapperAccessor<VisualShapeEntityComponent>(VisualShapeEntityComponent::class)
}
