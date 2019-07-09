package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class RemoveAfterDurationComponent(
    var secondsLeft: Float
) : Component {

    val initialSecondsLeft = secondsLeft

    companion object : ComponentMapperAccessor<RemoveAfterDurationComponent>(RemoveAfterDurationComponent::class)
}
