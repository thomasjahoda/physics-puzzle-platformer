package com.jafleck.game.desktop.util.tilededitor

import com.badlogic.gdx.Screen
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import ktx.app.KtxGame

/**
 * Hacky way so hacky TerrainGenerator has correct environment.
 */
object TerrainGeneratorStarter {

    fun useTilesetGenerator(consumer: (terrainGenerator: TerrainGenerator) -> Unit) {
        val config = LwjglApplicationConfiguration()
        val dummyApplication = DummyApplication(consumer)
        LwjglApplication(dummyApplication, config)
        dummyApplication.waitUntilFinished()
    }
}


/**
 * Dummy Gdx application so graphics environment is available while generating tileset.
 */
private class DummyApplication(private val consumer: (terrainGenerator: TerrainGenerator) -> Unit) : KtxGame<Screen>() {
    private var finished = false
    override fun create() {
        consumer(TerrainGenerator())
        dispose()
        finished = true
    }

    fun waitUntilFinished() {
        while (!finished) {
            // no judge pls
            Thread.sleep(10)
        }
    }
}
