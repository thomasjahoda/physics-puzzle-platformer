package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class PlayerComponent(
    var movementState: PlayerMovementState = PlayerMovementState.NONE
) : Component {

    companion object : ComponentMapperAccessor<PlayerComponent>(PlayerComponent::class)
}

enum class PlayerMovementState {
    LEFT,
    NONE,
    RIGHT
}
