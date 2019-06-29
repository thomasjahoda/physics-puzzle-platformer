package com.jafleck.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class GdxApplication : ApplicationAdapter() {
    private var batch: SpriteBatch? = null
    private var img: Texture? = null

    override fun create() {
        startKoin {
            printLogger()
            modules(createMainModule(this@GdxApplication))
        }
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.begin()
        batch!!.draw(img, 0f, 0f)
        batch!!.end()
    }

    override fun dispose() {
        batch?.dispose()
        img?.dispose()
        stopKoin()
    }
}
