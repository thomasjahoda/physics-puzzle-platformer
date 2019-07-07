package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.game.families.MovingBody


class SyncMovingBodySystem(
    priority: Int
) : IteratingSystem(MovingBody.family, priority) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val ce = MovingBody(entity)
        ce.position.vector.set(ce.body.value.position)
        ce.velocity.vector.set(ce.body.value.linearVelocity)
    }
}
