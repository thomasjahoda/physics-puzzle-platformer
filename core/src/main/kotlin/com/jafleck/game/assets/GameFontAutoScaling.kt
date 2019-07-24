package com.jafleck.game.assets

import com.badlogic.gdx.math.Vector2
import com.jafleck.game.util.logger
import ktx.math.div
import ktx.math.times

private val moduleLogger = logger("GameFontAutoScaling")


fun ScreenToWorldScalingPropagator.autoScaleGameFont(gameFont: GameFont) {
    scaleGameFont(gameFont, worldToScreenScalingFactor)
    worldToScreenScalingFactorListeners.addNewValueListener { newWorldToScreenScalingFactor ->
        scaleGameFont(gameFont, newWorldToScreenScalingFactor)
    }
}

private fun scaleGameFont(gameFont: GameFont, newWorldToScreenScalingFactor: Vector2) {
    val bitmapFont = gameFont.bitmapFont
    bitmapFont.data.setScale(1f, 1f) // reset scaling for floating point accuracy reasons

    val fontScaling = (newWorldToScreenScalingFactor * gameFont.worldFontSize) / gameFont.originalPixelFontSize
    moduleLogger.debug { "Scaling font $fontScaling" }
    gameFont.currentPixelFontSize = gameFont.originalPixelFontSize * fontScaling.y
    bitmapFont.data.setScale(fontScaling.x, fontScaling.y)
}

fun GameFont.autoScale(screenToWorldScalingPropagator: ScreenToWorldScalingPropagator) {
    screenToWorldScalingPropagator.autoScaleGameFont(this)
}
