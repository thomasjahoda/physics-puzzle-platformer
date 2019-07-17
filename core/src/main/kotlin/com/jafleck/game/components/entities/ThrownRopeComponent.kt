package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.families.PhysicalShapedEntity

class ThrownRopeComponent(
    val thrownBy: PhysicalShapedEntity,
    val throwerLocalAnchorPoint: Vector2,
    var anchoredBy: RevoluteJoint?
) : Component {

    val anchored
        get() = anchoredBy != null

    companion object : ComponentMapperAccessor<ThrownRopeComponent>(ThrownRopeComponent::class)
}
