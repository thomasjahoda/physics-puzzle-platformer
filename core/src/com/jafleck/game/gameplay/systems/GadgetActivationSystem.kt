package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.entities.ThrownBallEntityCreator
import com.jafleck.game.util.BasicGameGestureDetector
import com.jafleck.game.util.GameInputMultiplexer
import com.jafleck.game.util.GameViewport
import com.jafleck.game.util.systems.PlayerEntitySystem


class GadgetActivationSystem(
    priority: Int,
    private val gameViewport: GameViewport,
    private val gameInputMultiplexer: GameInputMultiplexer,
    private val thrownBallEntityCreator: ThrownBallEntityCreator
) : PlayerEntitySystem(priority) {

    private val gestureListener = GestureListener()
    private val basicGameGestureDetector = BasicGameGestureDetector(gestureListener)
    private val clickedWorldPositions = arrayListOf<Vector2>()

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        gameInputMultiplexer.addProcessor(basicGameGestureDetector)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameInputMultiplexer.removeProcessor(basicGameGestureDetector)
    }

    private inner class GestureListener : GestureDetector.GestureAdapter() {
        override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
            if (button == Input.Buttons.LEFT) {
                val screenPosition = Vector2(x, y)
                val worldClickPosition = gameViewport.unproject(screenPosition)
                clickedWorldPositions.add(worldClickPosition)
                return true
            } else {
                return false
            }
        }
    }

    override fun processPlayer(playerEntity: PlayerEntity) {
        clickedWorldPositions.forEach {
            // TODO extract to gadget class
            // TODO throw ball into this direction instead
            // TODO dispose balls after some time
            thrownBallEntityCreator.createThrownBall(it)
        }
        clickedWorldPositions.clear()
    }
}
