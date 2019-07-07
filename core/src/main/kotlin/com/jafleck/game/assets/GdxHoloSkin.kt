package com.jafleck.game.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.scene2d.Scene2DSkin

class GdxHoloSkin(private val assetManager: AssetManager) {

    fun setAsDefault() {
        Scene2DSkin.defaultSkin = assetManager.get(Assets.gdxHoloSkin)
    }
}
