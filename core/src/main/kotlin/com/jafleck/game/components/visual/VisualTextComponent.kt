package com.jafleck.game.components.visual

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.assets.GameFont

data class VisualTextComponent(
    var text: String,
    var color: Color,
    var gameFont: GameFont
) : Component {

    companion object : ComponentMapperAccessor<VisualTextComponent>(VisualTextComponent::class)
}
