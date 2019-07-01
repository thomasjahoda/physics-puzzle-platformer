package com.jafleck.game.util

import org.koin.core.logger.Level

fun convertLibgdxLoggingLevelToKoinLevel(logLevel: GdxLoggingLevel): Level? {
    return when (logLevel.asInt()) {
        GdxLoggingLevel.ERROR.asInt() -> Level.ERROR
        GdxLoggingLevel.INFO.asInt() -> Level.INFO
        GdxLoggingLevel.DEBUG.asInt() -> Level.DEBUG
        GdxLoggingLevel.NONE.asInt() -> null
        else -> throw IllegalArgumentException("unknown level ${logLevel}")
    }
}

fun GdxLoggingLevel.toKoinLoggingLevel() = convertLibgdxLoggingLevelToKoinLevel(this)
