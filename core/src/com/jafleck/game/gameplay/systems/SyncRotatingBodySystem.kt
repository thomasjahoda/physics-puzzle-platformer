package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.game.families.RotatingBody


class SyncRotatingBodySystem(
    priority: Int
) : IteratingSystem(RotatingBody.family, priority) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val ce = RotatingBody(entity)
        ce.rotation.radians = ce.body.value.angle
    }
}
