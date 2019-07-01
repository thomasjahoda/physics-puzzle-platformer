package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

class DrawableVisualComponent(
    val drawable: Drawable
) : Component {

    companion object : ComponentMapperAccessor<DrawableVisualComponent>(DrawableVisualComponent::class)
}
