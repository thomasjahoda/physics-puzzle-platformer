package com.jafleck.game.assets

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.jafleck.game.util.files.AssetsFileHandleResolver


class GameFontsLoader(
    private val assetsFileHandleResolver: AssetsFileHandleResolver,
    private val screenToWorldScalingPropagator: ScreenToWorldScalingPropagator
) {
    fun loadGameFonts(): GameFonts {
        val generator = FreeTypeFontGenerator(assetsFileHandleResolver.resolve("fonts/opensans/OpenSans-Regular.ttf"))

        val gameFonts = GameFonts(
            `regular 0_5f world size font` = generator.generateGameFont(12, 0.5f) {
                shadowColor = Color.BLACK
                shadowOffsetX = 1
                shadowOffsetY = 1
            },
            `regular 1f world size font` = generator.generateGameFont(12, 1f) {
                shadowColor = Color.BLACK
                shadowOffsetX = 1
                shadowOffsetY = 1
            }
        )

        generator.dispose()
        return gameFonts
    }

    private inline fun FreeTypeFontGenerator.generateGameFont(pixelFontSize: Int, worldFontSize: Float, parameterBlock: FreeTypeFontParameter.() -> Unit): GameFont {
        val parameter = FreeTypeFontParameter()
        parameter.size = pixelFontSize
        parameter.parameterBlock()

        val bitmapFont = this.generateFont(parameter)
        val gameFont = GameFont(bitmapFont, parameter.size, worldFontSize, parameter.size.toFloat())
        gameFont.autoScale(screenToWorldScalingPropagator)
        return gameFont
    }

    // note: could also use UI font: Scene2DSkin.defaultSkin.getFont("default-font")

}


class GameFonts(
    val `regular 0_5f world size font`: GameFont,
    val `regular 1f world size font`: GameFont
)
