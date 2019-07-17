package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class StickyRopePartComponent(
    var anchoredBy: RevoluteJoint?,
    var anchoredTo: Entity?
) : Component {

    val anchored
        get() = anchoredBy != null

    companion object : ComponentMapperAccessor<StickyRopePartComponent>(StickyRopePartComponent::class)
}
