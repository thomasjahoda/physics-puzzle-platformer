package com.jafleck.game.util

import com.badlogic.gdx.Application

inline class GdxLoggingLevel(private val logLevel: Int) {
    companion object {
        val ERROR = GdxLoggingLevel(Application.LOG_ERROR)
        val INFO = GdxLoggingLevel(Application.LOG_INFO)
        val DEBUG = GdxLoggingLevel(Application.LOG_DEBUG)
        val NONE = GdxLoggingLevel(Application.LOG_NONE)
    }

    fun asInt() = logLevel
}

var globalLoggingLevel: GdxLoggingLevel = GdxLoggingLevel.INFO
