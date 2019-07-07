package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.gadgets.MouseActivatedGadget
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.GameViewport
import com.jafleck.game.util.systems.PlayerEntitySystem


class PlayerGadgetActivationSystem(
    priority: Int,
    private val gameViewport: GameViewport,
    private val gameInputMultiplexer: GameInputMultiplexer
) : PlayerEntitySystem(priority) {

    private val basicGameGestureDetector = GestureDetector(GestureListener())
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
        clickedWorldPositions.forEach { targetPosition ->
            val gadget = playerEntity.selectedGadget.value
            if (gadget is MouseActivatedGadget) {
                gadget.activate(playerEntity.entity, targetPosition)
            }
        }
        clickedWorldPositions.clear()
    }
}
