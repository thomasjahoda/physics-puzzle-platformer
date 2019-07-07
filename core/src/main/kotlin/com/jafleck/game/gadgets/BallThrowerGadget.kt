package com.jafleck.game.gadgets

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.RectangleSizeComponent
import com.jafleck.game.entities.ThrownBallEntity
import com.jafleck.game.entities.ThrownBallEntityCreator
import ktx.math.*

class BallThrowerGadget(
    private val thrownBallEntityCreator: ThrownBallEntityCreator
) : MouseActivatedGadget {

    private val throwSpeed = 20f
    private val secondsAlive = 10f

    override fun activate(handler: Entity, targetPosition: Vector2) {
        val entityPosition = handler[OriginPositionComponent].vector
        val entityRectangleSize = handler[RectangleSizeComponent].vector

        val throwDirection = (targetPosition - entityPosition).nor()
        val velocity = throwDirection * throwSpeed
        val spawnPosition = entityPosition + throwDirection * (entityRectangleSize / 2 + ThrownBallEntity.HALF_SIZE)

        thrownBallEntityCreator.createThrownBall(spawnPosition, velocity).apply {
            addLimitedDuration(secondsAlive)
        }
    }

}
