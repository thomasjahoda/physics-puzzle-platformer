package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.World


class PhysicsSimulationStepSystem(
    private val world: World
) : EntitySystem() {

    override fun update(deltaSeconds: Float) {
        world.step(deltaSeconds, 6, 2)
    }
}
