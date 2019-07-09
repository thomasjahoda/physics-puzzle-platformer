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
    private val box2DDebugRenderer: Box2DDebugRenderer?
) : KtxScreen {

    init {
        stage.apply {
            // add UI here when necessary
            addActor(table {
                label("some UI text")
//                setFillParent(true)
                pack()
            })
        }
        uiInputMultiplexer.addProcessor(stage)
        uiInputMultiplexer.addProcessor(gameInputMultiplexer)
    }

    override fun show() {
        Gdx.input.inputProcessor = uiInputMultiplexer
    }

    override fun render(delta: Float) {
        updateAndRender(delta)
    }

    private fun updateAndRender(deltaSeconds: Float) {
        clearScreenAndRenderUi()

        gameViewport.apply()
        engine.update(deltaSeconds)
        box2DDebugRenderer?.render(world, gameCamera.combined)
    }

    private fun clearScreenAndRenderUi() {
        uiViewport.apply()
        clearScreen(Color.WHITE)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, false)
        uiViewport.update(width, height, true)

        gameViewport.camera.update()
        screenToWorldScalingPropagator.scaling = Vector2(gameViewport.camera.combined.scaleX, gameViewport.camera.combined.scaleY)
    }

    override fun hide() {
    }
}
