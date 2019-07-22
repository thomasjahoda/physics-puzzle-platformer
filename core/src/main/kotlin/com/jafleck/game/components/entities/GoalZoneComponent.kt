package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class GoalZoneComponent(
    var progress: Float = 0f
) : Component {

    companion object : ComponentMapperAccessor<GoalZoneComponent>(GoalZoneComponent::class)
}
