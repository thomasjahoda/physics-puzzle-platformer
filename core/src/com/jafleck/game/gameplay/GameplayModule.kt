package com.jafleck.game.gameplay

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.jafleck.game.assets.Drawables
import com.jafleck.game.components.DrawableVisualComponent
import com.jafleck.game.components.PositionComponent
import com.jafleck.game.components.RectangleSizeComponent
import com.jafleck.game.gameplay.systems.RenderDrawableRectangleComponentsSystem
import com.jafleck.game.gameplay.ui.PlayScreen
import org.koin.core.module.Module
import org.koin.dsl.module

fun createModuleLoadingLevelForGameplay(): Module {
    return module {
        single { OrthographicCamera() }
        single { ScreenViewport(get(OrthographicCamera::class, null, null)) }
        single { Stage(get(ScreenViewport::class, null, null)) }
        single { SpriteBatch() }
        single {
            val engine = Engine().apply {
                var systemPriority = 0
                @Suppress("UNUSED_CHANGED_VALUE")
                addSystem(RenderDrawableRectangleComponentsSystem(systemPriority++, get(), get()))
            }
            loadLevel(engine)

            engine
        }
        single { PlayScreen(get(), get()) }
    }
}

private fun loadLevel(engine: Engine) {
    // hard-coded level
    // load level from file
    engine.addEntity(Entity().apply {
        add(PositionComponent(0f, 0f))
        add(RectangleSizeComponent(50f, 100f))
        add(DrawableVisualComponent(Drawables.player))
    })

    engine.addEntity(Entity().apply {
        add(PositionComponent(300f, 20f))
        add(RectangleSizeComponent(100f, 100f))
        add(DrawableVisualComponent(Drawables.player))
    })
}
