package com.jafleck.game.util.listeners

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family

interface EntityFamilyListener : EntityListener {
    val family: Family

    override fun entityAdded(entity: Entity)

    override fun entityRemoved(entity: Entity)
}
