package com.jafleck.game.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object Drawables {
    val player = TextureRegionDrawable(Texture(Gdx.files.internal("graphics/player.png")))

}
