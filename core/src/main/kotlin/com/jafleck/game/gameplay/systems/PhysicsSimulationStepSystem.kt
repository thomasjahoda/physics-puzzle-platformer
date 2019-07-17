package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.World


class PhysicsSimulationStepSystem(
    private val world: World
) : EntitySystem() {

    override fun update(deltaSeconds: Float) {
        // increased iterations for more accurate for many joints, e.g. in ropes. Same config is used in box2d manual example
        world.step(deltaSeconds, 10, 8)
    }
}
