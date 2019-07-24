package com.jafleck.game.gameplay.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.jafleck.extensions.libgdxktx.clearScreen
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.gameplay.controlandmainphases.GameLogicTickExecutor
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.input.UiInputMultiplexer
import com.jafleck.game.util.logger
import ktx.app.KtxScreen
import ktx.math.minus
import ktx.scene2d.table
import kotlin.math.max


class PlayScreen(
    private val stage: Stage,
    private val uiViewport: UiViewport,
    private val gameViewport: GameViewport,
    private val screenToWorldScalingPropagator: ScreenToWorldScalingPropagator,
    private val gameInputMultiplexer: GameInputMultiplexer,
    private val uiInputMultiplexer: UiInputMultiplexer,
    private val menuRow: MenuRow,
    private val debugRow: DebugRow,
    private val gameLogicTickExecutor: GameLogicTickExecutor,
    private val manualTimeControl: ManualTimeControl?,
    private val fpsCounter: FpsCounter?
) : KtxScreen {

    private val logger = logger(this::class)

    private val rootTable = table {
        setFillParent(true)

        add(menuRow.content)
            .expandX().fillX()
            .expandY().fillY()
        row()
        add(debugRow.content).expandX().fillX().bottom()
    }

    init {
        stage.apply {
            addActor(rootTable)
        }
        uiInputMultiplexer.addProcessor(stage)
        uiInputMultiplexer.addProcessor(gameInputMultiplexer)
    }

    override fun show() {
        Gdx.input.inputProcessor = uiInputMultiplexer
    }

    override fun render(delta: Float) {
        fpsCounter?.calculateFps(delta)
        val deltaSeconds: Float = manualTimeControl?.transformDeltaTime(delta) ?: delta
        updateAndRender(deltaSeconds)
    }

    private fun updateAndRender(deltaSeconds: Float) {
        clearScreen(Color.WHITE)
        gameLogicTickExecutor.updateAndRenderTick(deltaSeconds)
        renderUi(deltaSeconds)
    }

    private fun renderUi(deltaSeconds: Float) {
        uiViewport.apply()
//        stage.isDebugAll = true
        stage.act(deltaSeconds)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, false)
        uiViewport.update(width, height, true)

        val worldToScreenCoordinateScalar = (gameViewport.project(Vector2(1f, 1f)) - gameViewport.project(Vector2(0f, 0f))).let {
            max(it.x, it.y) // mitigate rounding issues
        }
        logger.debug { "New worldToScreenCoordinateScalar $worldToScreenCoordinateScalar" }
        screenToWorldScalingPropagator.worldToScreenScalingFactor = Vector2(worldToScreenCoordinateScalar, worldToScreenCoordinateScalar)
    }

    override fun hide() {
    }
}
