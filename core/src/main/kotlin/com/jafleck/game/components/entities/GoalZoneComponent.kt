package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.Color
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class GoalZoneComponent(
    /**
     * from 0 to 1
     */
    var progress: Float = 0f,
    val startColor: Color,
    val endColor: Color
) : Component {

    companion object : ComponentMapperAccessor<GoalZoneComponent>(GoalZoneComponent::class)
}
