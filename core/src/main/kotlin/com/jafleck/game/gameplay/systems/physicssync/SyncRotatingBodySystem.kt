package com.jafleck.game.gameplay.systems.physicssync

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.game.families.RotatingBody


class SyncRotatingBodySystem : IteratingSystem(RotatingBody.family) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val ce = RotatingBody(entity)
        ce.rotation.radians = ce.body.value.angle
    }
}
