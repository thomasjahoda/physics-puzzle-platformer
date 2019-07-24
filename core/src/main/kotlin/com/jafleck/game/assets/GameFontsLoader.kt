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
        val generator = FreeTypeFontGenerator(assetsFileHandleResolver.resolve("fonts/opensans/OpenSans-Bold.ttf"))

        val gameFonts = GameFonts(
            `bold 0_5f world size font` = generator.generateGameFont(16, 0.5f) {
                blackThickBorder()
                blackShadow()
            },
            `bold 1f world size font` = generator.generateGameFont(32, 1f) {
                blackThickBorder()
                blackShadow()
            }
        )

        generator.dispose()
        return gameFonts
    }

    private fun FreeTypeFontParameter.blackThickBorder() {
        borderColor = Color.BLACK
        borderWidth = 1f
    }

    private fun FreeTypeFontParameter.blackShadow() {
        shadowColor = Color.BLACK
        shadowOffsetX = 1
        shadowOffsetY = 1
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
    val `bold 0_5f world size font`: GameFont,
    val `bold 1f world size font`: GameFont
)
