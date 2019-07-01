package com.jafleck.game.util.ui

import com.badlogic.gdx.assets.AssetManager
import com.jafleck.game.assets.Assets
import ktx.scene2d.Scene2DSkin

class GdxHoloSkin(private val assetManager: AssetManager) {

    fun setAsDefault() {
        Scene2DSkin.defaultSkin = assetManager.get(Assets.gdxHoloSkin)
    }
}
