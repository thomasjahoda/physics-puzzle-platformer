package com.jafleck.game.gameplay.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.jafleck.extensions.libgdxktx.clearScreen
import ktx.app.KtxScreen
import ktx.scene2d.label
import ktx.scene2d.table


@Suppress("ConstantConditionIf")
class PlayScreen(
    private val stage: Stage
) : KtxScreen {

    init {
        stage.apply {
            val rootTable = table {
                label("test")
            }
            rootTable.setFillParent(true)
            addActor(rootTable)

            // example from https://libgdx.info/basic_image/
            val texture = Texture(Gdx.files.internal("player.png"))
            val image = Image(texture)
//            image.setPosition(Gdx.graphics.width / 3 - image.width / 2, Gdx.graphics.height * 2 / 3 - image.height / 2)
            image.setPosition(0f, 0f)
            addActor(image)
        }
    }

    override fun render(delta: Float) {
        update(delta)
        draw(delta)
    }

    private fun update(delta: Float) {
        // TODO update game logic, but if systems draw entities this has to be called at draw instead
    }

    private fun draw(delta: Float) {
        clearScreen(Color.WHITE)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, false)
    }

    override fun hide() {
    }
}
