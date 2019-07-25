package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class VisualShapeComponent : Component {

    companion object : ComponentMapperAccessor<VisualShapeComponent>(VisualShapeComponent::class)
}
