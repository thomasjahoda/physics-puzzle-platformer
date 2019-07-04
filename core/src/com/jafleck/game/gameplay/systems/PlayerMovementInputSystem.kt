package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.components.PlayerMovementState
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.util.GameViewport
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.input.GestureAdapterInterface
import com.jafleck.game.util.input.InputProcessorAdapter
import com.jafleck.game.util.systems.PlayerEntitySystem


class PlayerMovementInputSystem(
    priority: Int,
    private val gameViewport: GameViewport,
    private val gameInputMultiplexer: GameInputMultiplexer
) : PlayerEntitySystem(priority) {

    private val logger = ktx.log.logger<PlayerMovementInputSystem>()

    private val gestureListener = GestureListener()
    private val basicGameGestureDetector = GestureDetector(
        20f,
        0.4f,
        0.2f, // lowered longPressDuration from 1.1f to 0.2f
        0.15f,
        gestureListener)

    private val lastKnownPlayerPosition = Vector2()
    private var currentMovementState = PlayerMovementState.NONE

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        gameInputMultiplexer.addProcessor(basicGameGestureDetector)
        gameInputMultiplexer.addProcessor(gestureListener)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        gameInputMultiplexer.removeProcessor(basicGameGestureDetector)
        gameInputMultiplexer.removeProcessor(gestureListener)
    }

    private inner class GestureListener : InputProcessorAdapter, GestureAdapterInterface {
        private var ongoingLongPress = false
        private var movementKeyDown = false

        override fun longPress(x: Float, y: Float): Boolean {
            val screenPosition = Vector2(x, y)
            val worldClickPosition = gameViewport.unproject(screenPosition)

            determineMovementDuringLongPressOn(worldClickPosition)
            ongoingLongPress = true
            return true
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            val screenPosition = Vector2(screenX.toFloat(), screenY.toFloat())
            val worldClickPosition = gameViewport.unproject(screenPosition)

            if (ongoingLongPress) {
                determineMovementDuringLongPressOn(worldClickPosition)
                return true
            } else {
                return false
            }
        }

        private fun determineMovementDuringLongPressOn(worldClickPosition: Vector2) {
            if (!movementKeyDown) {
                if (worldClickPosition.x < lastKnownPlayerPosition.x) {
                    currentMovementState = PlayerMovementState.LEFT
                } else if (worldClickPosition.x > lastKnownPlayerPosition.x) {
                    currentMovementState = PlayerMovementState.RIGHT
                }
            }
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            if (ongoingLongPress) {
                ongoingLongPress = false
                if (!movementKeyDown) {
                    currentMovementState = PlayerMovementState.NONE
                }
                return true
            } else {
                return false
            }
        }

        override fun keyDown(keycode: Int): Boolean {
            return when (keycode) {
                Input.Keys.A -> {
                    currentMovementState = PlayerMovementState.LEFT
                    movementKeyDown = true
                    true
                }
                Input.Keys.D -> {
                    currentMovementState = PlayerMovementState.RIGHT
                    movementKeyDown = true
                    true
                }
                else -> false
            }
        }

        override fun keyUp(keycode: Int): Boolean {
            return when (keycode) {
                Input.Keys.A -> {
                    if (!Gdx.input.isKeyPressed(Input.Keys.D)) {
                        currentMovementState = PlayerMovementState.NONE
                        movementKeyDown = false
                    }
                    true
                }
                Input.Keys.D -> {
                    if (!Gdx.input.isKeyPressed(Input.Keys.A)) {
                        currentMovementState = PlayerMovementState.NONE
                        movementKeyDown = false
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun processPlayer(playerEntity: PlayerEntity) {
        val playerPosition = playerEntity.position.vector
        lastKnownPlayerPosition.set(playerPosition)

        if (playerEntity.player.movementState != currentMovementState) {
            logger.debug { "Changed player movement from ${playerEntity.player.movementState} to $currentMovementState" }
        }

        playerEntity.player.movementState = currentMovementState
    }
}
