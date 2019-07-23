package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.jafleck.game.components.entities.GoalZoneComponent
import com.jafleck.game.entities.GoalZoneEntity
import com.jafleck.game.gameplay.controlandmainphases.FinishedMapSuccessfullyHandler
import com.jafleck.game.util.logger
import ktx.ashley.allOf
import kotlin.math.max


class GoalZoneSystem(
    private val finishedMapSuccessfullyHandler: FinishedMapSuccessfullyHandler
) : IteratingSystem(allOf(GoalZoneComponent::class).get()) {

    companion object {
        private const val PROGRESS_PER_SECOND = 0.5f
    }

    private val logger = logger(this::class)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val typedEntity = GoalZoneEntity(entity)
        val goalZone = typedEntity.goalZone
        if (typedEntity.entityCollisionTrackingZone.entitiesWithinZone.isNotEmpty()) {
            val progressOfThisTick = getProgressFor(deltaTime)
            goalZone.progress += progressOfThisTick
            if (goalZone.progress >= 1f) {
                logger.debug { "Goal zone progress is full, will finish level" }
                finishedMapSuccessfullyHandler.onMapSuccessfullyFinished()
            } else {
                logger.debug { "Goal zone entity ${typedEntity.entity} is currently at ${goalZone.progress} (added $progressOfThisTick this tick)" }
            }
        } else {
            if (goalZone.progress != 0f) {
                goalZone.progress = max(0f, goalZone.progress - getProgressFor(deltaTime))
            }
        }
    }

    private fun getProgressFor(deltaSeconds: Float) = PROGRESS_PER_SECOND * deltaSeconds
}
