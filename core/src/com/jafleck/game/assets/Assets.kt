package com.jafleck.game.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

object Assets {
    val atlas = AssetDescriptor("atlas/textures.atlas", TextureAtlas::class.java)
    val gdxHoloSkin = AssetDescriptor("gdx-holo-skin/uiskin.json", Skin::class.java)

    fun queueAssetsToLoad(assetManager: AssetManager) {
        assetManager.run {
            load(atlas)
            load(gdxHoloSkin)
        }
    }
}
