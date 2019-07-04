package com.jafleck.game.gadgets

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.entities.ThrownBallEntityCreator

class BallThrowerGadget(
    private val thrownBallEntityCreator: ThrownBallEntityCreator
) : MouseActivatedGadget {

    override fun activate(handler: Entity, targetPosition: Vector2) {
        thrownBallEntityCreator.createThrownBall(targetPosition)
    }

}
