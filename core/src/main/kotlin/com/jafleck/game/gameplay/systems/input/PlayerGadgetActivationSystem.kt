package com.jafleck.game.gameplay.systems.input

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.gadgets.MouseActivatedGadget
import com.jafleck.game.gameplay.systems.PlayerEntitySystem
import com.jafleck.game.gameplay.ui.GameViewport
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.logger


class PlayerGadgetActivationSystem(
    private val gameViewport: GameViewport,
    private val gameInputMultiplexer: GameInputMultiplexer
) : PlayerEntitySystem() {

    private val basicGameGestureDetector = GestureDetector(GestureListener())
    private var lastClickedWorldPosition: Vector2? = null

    private val logger = logger(this::class)

    override fun additionalAddedToEngine(engine: Engine) {
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
                lastClickedWorldPosition = worldClickPosition
                return true
            } else {
                return false
            }
        }
    }

    override fun processPlayer(playerEntity: PlayerEntity, deltaSeconds: Float) {
        if (deltaSeconds == 0f) return

        if (lastClickedWorldPosition != null) {
            val gadget = playerEntity.gadgetHolder.selectedGadget
            if (gadget is MouseActivatedGadget) {
                logger.debug { "Activating gadget at world position $lastClickedWorldPosition" }
                gadget.activate(playerEntity.entity, lastClickedWorldPosition!!)
            }
            lastClickedWorldPosition = null
        }
    }
}
