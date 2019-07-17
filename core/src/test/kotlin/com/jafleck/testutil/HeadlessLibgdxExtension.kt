package com.jafleck.testutil

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader
import com.badlogic.gdx.graphics.GL20
import io.mockk.mockk
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext


class HeadlessLibgdxExtension : BeforeAllCallback, AfterAllCallback {

    private lateinit var application: Application
    override fun beforeAll(context: ExtensionContext?) {
        application = HeadlessApplication(object : ApplicationListener {
            override fun create() {}
            override fun resize(width: Int, height: Int) {}
            override fun render() {}
            override fun pause() {}
            override fun resume() {}
            override fun dispose() {}
        })
        val gl20 = mockk<GL20>(relaxed = true)
        Gdx.gl = gl20
        Gdx.gl20 = gl20
    }

    override fun afterAll(context: ExtensionContext?) {
        application.exit()
    }

}
