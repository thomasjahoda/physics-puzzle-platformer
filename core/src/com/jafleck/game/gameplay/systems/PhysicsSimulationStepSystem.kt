package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.World


class PhysicsSimulationStepSystem(
    priority: Int,
    private val world: World
) : EntitySystem(priority) {

    override fun update(deltaSeconds: Float) {
        world.step(deltaSeconds, 6, 2)
    }
}
