package com.jafleck.game.components.logic

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

data class RemoveAfterDurationComponent(
    var secondsLeft: Float
) : Component {

    val initialSecondsLeft = secondsLeft

    companion object : ComponentMapperAccessor<RemoveAfterDurationComponent>(RemoveAfterDurationComponent::class)
}
