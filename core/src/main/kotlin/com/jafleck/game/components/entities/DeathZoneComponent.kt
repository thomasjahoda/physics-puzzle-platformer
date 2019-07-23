package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class DeathZoneComponent : Component {

    companion object : ComponentMapperAccessor<DeathZoneComponent>(DeathZoneComponent::class)
}
