package com.jafleck.game

import com.badlogic.gdx.Screen
import com.jafleck.game.gameplay.createModuleLoadingLevelForGameplay
import com.jafleck.game.gameplay.ui.PlayScreen
import com.jafleck.game.util.ui.GdxHoloSkin
import ktx.app.KtxGame
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

/**
 * Main class of game, delegates different aspects to other modules.
 */
class GdxApplication : KtxGame<Screen>() {

    override fun create() {
        GdxHoloSkin.registerAsDefault()
        val koinApplication = startKoin {
            printLogger()
            modules(listOf(
                createMainModule(this@GdxApplication),
                createModuleLoadingLevelForGameplay()
            ))
        }
        val screen = koinApplication.koin.get<PlayScreen>()
        addScreen(screen)
        setScreen<PlayScreen>()
    }

    override fun dispose() {
        super.dispose()
        stopKoin()
    }
}
