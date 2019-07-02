package com.jafleck.game.config

import com.jafleck.game.util.LoggingLevel
import com.jafleck.game.util.asGdxLoggingLevel
import com.jafleck.game.util.asKoinLoggingLevel

object LoggingConfig {
    val defaultLoggingLevel = getLoggingLevel("GENERAL_LOGGING_LEVEL", default = LoggingLevel.NONE)
    val gdxLoggingLevel = getLoggingLevel("GDX_LOGGING_LEVEL", default = defaultLoggingLevel).asGdxLoggingLevel()
    val gameLoggingLevel = getLoggingLevel("GAME_LOGGING_LEVEL", default = defaultLoggingLevel)
    val koinLoggingLevel = getLoggingLevel("KOIN_LOGGING_LEVEL", default = defaultLoggingLevel).asKoinLoggingLevel()

    private fun getLoggingLevel(envName: String, default: LoggingLevel) =
        if (System.getenv(envName) != null) LoggingLevel.valueOf(System.getenv(envName).toUpperCase())
        else default

}

