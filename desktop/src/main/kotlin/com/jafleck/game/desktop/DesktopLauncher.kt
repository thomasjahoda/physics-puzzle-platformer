@file:JvmName("DesktopLauncher")
@file:Suppress("UnusedMainParameter")

package com.jafleck.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.jafleck.game.GdxApplication


fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.samples = 3
    config.vSyncEnabled = true // set to false to find out what the maximum FPS are
    config.foregroundFPS = 0 // whatever is possible (if vsync is enabled, uses it's refresh rate as max)
    LwjglApplication(GdxApplication(), config)
}

