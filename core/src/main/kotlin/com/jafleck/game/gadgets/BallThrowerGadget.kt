package com.jafleck.game.gadgets

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.entities.ThrownBallEntity
import com.jafleck.game.entities.ThrownBallEntityCreator
import com.jafleck.game.families.ShapedEntity
import com.jafleck.game.util.logger
import ktx.math.div
import ktx.math.minus
import ktx.math.plus
import ktx.math.times

class BallThrowerGadget(
    private val thrownBallEntityCreator: ThrownBallEntityCreator
) : MouseActivatedGadget {

    private val throwSpeed = 20f
    private val secondsAlive = 10f

    private val logger = logger(this::class)

    companion object {
        const val NAME = "BallThrower"
    }

    override val name: String
        get() = NAME

    override fun activate(handler: Entity, targetPosition: Vector2) {
        val shapedEntity = ShapedEntity(handler)
        val entityRectangleSize = shapedEntity.shape.getRectangleAroundShape(Vector2())
        val entityPosition = handler[OriginPositionComponent].vector

        val throwDirection = (targetPosition - entityPosition).nor()
        val velocity = throwDirection * throwSpeed
        val spawnPosition = entityPosition + throwDirection * (entityRectangleSize / 2 + ThrownBallEntity.HALF_SIZE)

        logger.debug { "Spawning ball at $spawnPosition with velocity $velocity" }
        thrownBallEntityCreator.createThrownBall(spawnPosition, velocity).apply {
            addLimitedDuration(secondsAlive)
        }
    }

}
