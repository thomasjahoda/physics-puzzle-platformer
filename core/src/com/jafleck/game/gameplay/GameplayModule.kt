package com.jafleck.game.gameplay

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.jafleck.game.gameplay.ui.PlayScreen
import org.koin.core.module.Module
import org.koin.dsl.module

fun createModuleLoadingLevelForGameplay(): Module {
    return module {
        single { ScreenViewport() }
        single { Stage(get(ScreenViewport::class, null, null)) }
        single { PlayScreen(get())}
    }
}
