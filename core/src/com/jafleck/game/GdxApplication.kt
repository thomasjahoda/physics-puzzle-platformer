package com.jafleck.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.jafleck.game.gameplay.MapLoader
import com.jafleck.game.gameplay.createGameplayModule
import com.jafleck.game.gameplay.ui.PlayScreen
import com.jafleck.game.util.GdxLoggingLevel
import com.jafleck.game.util.globalLoggingLevel
import com.jafleck.game.util.toKoinLoggingLevel
import com.jafleck.game.util.ui.GdxHoloSkin
import ktx.app.KtxGame
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

/**
 * Main class of game, delegates different aspects to other modules.
 */
class GdxApplication : KtxGame<Screen>() {

    override fun create() {
        val loggingLevel = GdxLoggingLevel.INFO
        Gdx.app.logLevel = loggingLevel.asInt()
        globalLoggingLevel = loggingLevel

        val koinApplication = startKoin {
            val koinLoggingLevel = loggingLevel.toKoinLoggingLevel()
            if (koinLoggingLevel != null) {
                printLogger(koinLoggingLevel)
            }
            modules(listOf(
                createMainModule(this@GdxApplication),
                createGameplayModule()
            ))
        }

        koinApplication.koin.get<GdxHoloSkin>().setAsDefault()

        val mapLoader = koinApplication.koin.get<MapLoader>()
        mapLoader.loadMap()

        val screen = koinApplication.koin.get<PlayScreen>()
        addScreen(screen)
        setScreen<PlayScreen>()
    }

    override fun dispose() {
        super.dispose()
        stopKoin()
    }
}
