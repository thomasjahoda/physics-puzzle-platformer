package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.game.util.logger


class PhysicsSimulationStepSystem(
    private val world: World
) : EntitySystem() {

    private val logger = logger(this::class)

    override fun update(deltaSeconds: Float) {
        if (deltaSeconds == 0f) return

        // increased iterations for more accurate for many joints, e.g. in ropes. Same config is used in box2d manual example
        logger.debug { "Starting physics simulation" }
        world.step(deltaSeconds, 10, 8)
        logger.debug { "Ended physics simulation" }
    }
}
