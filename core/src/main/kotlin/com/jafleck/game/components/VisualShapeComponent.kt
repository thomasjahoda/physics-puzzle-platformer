package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class VisualShapeComponent(
    var borderColor: Color?,
    var borderThickness: Float?,
    var fillColor: Color?
) : Component {

    init {
        if (borderThickness != null) {
            require(borderColor != null) { "Border color must be set if thickness is set" }
        }
    }

    companion object : ComponentMapperAccessor<VisualShapeComponent>(VisualShapeComponent::class)
}
