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
import ktx.app.KtxScreen
import ktx.scene2d.table


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

        screenToWorldScalingPropagator.scaling = Vector2(gameViewport.camera.combined.scaleX, gameViewport.camera.combined.scaleY)
    }

    override fun hide() {
    }
}
