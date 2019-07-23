package com.jafleck.game.gameplay.standaloneentitylisteners

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.game.entities.ActiveGameMapEntity
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.gameplay.controlandmainphases.MapReloader
import com.jafleck.game.util.ashley.EntityFamilyListener


class PlayerDeathEntityListener(
    private val mapReloader: MapReloader,
    private val engine: Engine
) : EntityFamilyListener {

    override val family: Family
        get() = PlayerEntity.family

    override fun entityAdded(entity: Entity) {
    }

    override fun entityRemoved(entity: Entity) {
        if (ActiveGameMapEntity.hasActiveMap(engine)) {
            mapReloader.reloadMapAfterSystemProcessing()
        }
    }
}
