package com.jafleck.game.util

import com.badlogic.gdx.Application
import ktx.log.Logger
import org.koin.core.logger.Level
import kotlin.reflect.KClass

enum class LoggingLevel {
    ERROR,
    INFO,
    DEBUG,
    NONE;
}

private val gdxLoggingLevelByLoggingLevel = mapOf(
    LoggingLevel.ERROR to Application.LOG_ERROR,
    LoggingLevel.INFO to Application.LOG_INFO,
    LoggingLevel.DEBUG to Application.LOG_DEBUG,
    LoggingLevel.NONE to Application.LOG_NONE
)

fun LoggingLevel.asGdxLoggingLevel() = gdxLoggingLevelByLoggingLevel[this] ?: error("unknown logging level ${this}")


private val koinLoggingLevelByLoggingLevel = mapOf(
    LoggingLevel.ERROR to Level.ERROR,
    LoggingLevel.INFO to Level.INFO,
    LoggingLevel.DEBUG to Level.DEBUG,
    LoggingLevel.NONE to null
)

fun LoggingLevel.asKoinLoggingLevel() = koinLoggingLevelByLoggingLevel[this]

fun logger(someClass: KClass<out Any>): Logger = Logger(someClass.java.name)

fun logger(name: String): Logger = Logger(name)
