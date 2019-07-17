package com.jafleck.game.config

object GeneralDebugConfiguration {
    val manualTimeControlEnabled = System.getenv("MANUAL_TIME_CONTROL_ENABLED")?.toBoolean() ?: false
}

