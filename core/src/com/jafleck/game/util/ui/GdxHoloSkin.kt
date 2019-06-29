package com.jafleck.game.util.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.Scene2DSkin

object GdxHoloSkin {

    fun registerAsDefault() {
        Scene2DSkin.defaultSkin = load()
    }

    fun load() = Skin(Gdx.files.internal("gdx-holo-skin/uiskin.json"))
}
