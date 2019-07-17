package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.families.PositionedPlayer

abstract class PlayerEntitySystem : EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    final override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(PositionedPlayer.family)
        additionalAddedToEngine(engine)
        process(0f)
    }

    protected open fun additionalAddedToEngine(engine: Engine) {
    }

    final override fun update(deltaSeconds: Float) {
        process(deltaSeconds)
    }

    private fun process(deltaSeconds: Float) {
        require(entities.size() <= 1) { "Only one player may exist at one time but ${entities.size()} were found" }
        if (entities.size() == 1) {
            val entity = PlayerEntity(entities.get(0))
            processPlayer(entity, deltaSeconds)
        }
    }

    protected abstract fun processPlayer(playerEntity: PlayerEntity, deltaSeconds: Float)

}
