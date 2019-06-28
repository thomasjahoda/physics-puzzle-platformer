@file:JvmName("DesktopLauncher")
@file:Suppress("UnusedMainParameter")

package com.jafleck.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.jafleck.game.PhysicsPuzzlePlatformer


fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(PhysicsPuzzlePlatformer(), config)
}

