package com.jafleck.game.preferences

import com.badlogic.gdx.Gdx
import org.koin.dsl.module

val preferencesModule = module {
    single { Gdx.app.getPreferences("main") }
}
