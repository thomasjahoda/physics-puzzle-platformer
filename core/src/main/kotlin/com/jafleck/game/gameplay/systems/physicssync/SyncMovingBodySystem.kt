package com.jafleck.game.gameplay.systems.physicssync

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.game.families.MovingBody


class SyncMovingBodySystem() : IteratingSystem(MovingBody.family) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val ce = MovingBody(entity)
        ce.position.vector.set(ce.body.value.position)
        ce.velocity.vector.set(ce.body.value.linearVelocity)
    }
}
