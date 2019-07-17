package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class VisualDebugMarkerComponent(
    val text: String?
) : Component {

    companion object : ComponentMapperAccessor<VisualDebugMarkerComponent>(VisualDebugMarkerComponent::class)
}
