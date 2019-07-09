package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.families.PositionedPlayer

abstract class PlayerEntitySystem(
    priority: Int
) : EntitySystem(priority) {

    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(PositionedPlayer.family)
        process()
    }

    override fun update(deltaSeconds: Float) {
        process()
    }

    private fun process() {
        require(entities.size() <= 1) { "Only one player may exist at one time but ${entities.size()} were found" }
        if (entities.size() == 1) {
            val entity = PlayerEntity(entities.get(0))
            processPlayer(entity)
        }
    }

    protected abstract fun processPlayer(playerEntity: PlayerEntity)

}
