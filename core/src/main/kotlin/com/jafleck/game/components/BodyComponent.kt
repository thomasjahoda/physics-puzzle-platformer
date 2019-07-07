package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.Body
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class BodyComponent(
    val value: Body
) : Component {
    companion object : ComponentMapperAccessor<BodyComponent>(BodyComponent::class)
}
