package com.jafleck.game.gameplay.ui

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.jafleck.game.assets.Assets

class TransparentColorBackgroundImageFactory(
    private val assetManager: AssetManager
) {
    fun createColorBackground(color: Color): Image {
        return Image(assetManager.get(Assets.atlas).findRegion("single-pixel")).apply {
            this.color = color
        }
    }
}
