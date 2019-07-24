package com.jafleck.game.assets

import com.badlogic.gdx.graphics.g2d.BitmapFont

/**
 * BitmapFont that is used within the game world (world coordinates).
 */
data class GameFont(
    val bitmapFont: BitmapFont,
    val originalPixelFontSize: Int,
    val worldFontSize: Float,
    var currentPixelFontSize: Float
)
