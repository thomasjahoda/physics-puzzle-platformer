package com.jafleck.game.gameplay.ui

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.jafleck.extensions.libgdxktx.clearScreen
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.gameplay.systems.debug.CursorDebugSystem
import com.jafleck.game.util.input.GameInputMultiplexer
import com.jafleck.game.util.input.UiInputMultiplexer
import ktx.app.KtxScreen
import ktx.scene2d.label
import ktx.scene2d.table


class PlayScreen(
    private val stage: Stage,
    private val engine: Engine,
    private val world: World,
    private val gameCamera: GameCamera,
    private val gameViewport: GameViewport,
    private val uiCamera: UiCamera,
    private val uiViewport: UiViewport,
    private val screenToWorldScalingPropagator: ScreenToWorldScalingPropagator,
    private val gameInputMultiplexer: GameInputMultiplexer,
    private val uiInputMultiplexer: UiInputMultiplexer,
    private val box2DDebugRenderer: Box2DDebugRenderer?,
    private val cursorDebugSystem: CursorDebugSystem?,
    private val manualTimeControl: ManualTimeControl?,
    private val fpsCounter: FpsCounter?
) : KtxScreen {

    private val rootTable = table {
        if (cursorDebugSystem != null) {
            add(label("  "))
            add(cursorDebugSystem.worldCoordsOfCursorLabel)
        }
        if (fpsCounter != null) {
            add(label("   "))
            add(fpsCounter.fpsLabel)
        }
//        setFillParent(true)
        pack()
    }

    init {
        stage.apply {
            // add UI here when necessary
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

        gameViewport.apply()
        engine.update(deltaSeconds)
        box2DDebugRenderer?.render(world, gameCamera.combined)
        renderUi()
    }

    private fun renderUi() {
        uiViewport.apply()
        rootTable.pack()
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
