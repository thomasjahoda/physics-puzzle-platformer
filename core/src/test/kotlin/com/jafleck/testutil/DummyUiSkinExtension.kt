package com.jafleck.testutil

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.jafleck.game.assets.Assets
import com.jafleck.game.assets.GdxHoloSkin
import ktx.scene2d.Scene2DSkin
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext


class DummyUiSkinExtension : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext?) {
        require(Gdx.gl20 != null) { "HeadlessLibgdxExtension must be running"}

        LwjglNativesLoader.load()

        val assetManager = AssetManager(CustomClasspathAssetsFileHandleResolver())
        assetManager.load(Assets.gdxHoloSkin)

        assetManager.update(10 * 1000)
        GdxHoloSkin(assetManager).setAsDefault()
    }

    override fun afterAll(context: ExtensionContext?) {
    }

}
