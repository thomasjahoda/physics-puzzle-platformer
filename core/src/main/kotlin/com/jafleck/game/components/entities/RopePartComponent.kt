package com.jafleck.game.components.entities

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint
import com.badlogic.gdx.physics.box2d.joints.RopeJoint
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.entities.RopeEntity

class RopePartComponent(
    val owningRopeEntity: RopeEntity,
    var anchoredToNextPartBy: RevoluteJoint?,
    var restrictedToNextPartBy: RopeJoint?
) : Component {

    companion object : ComponentMapperAccessor<RopePartComponent>(RopePartComponent::class)
}
