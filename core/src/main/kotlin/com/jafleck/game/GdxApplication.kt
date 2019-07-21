package com.jafleck.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.jafleck.game.assets.GdxHoloSkin
import com.jafleck.game.assets.assetsModule
import com.jafleck.game.config.LoggingConfig
import com.jafleck.game.entities.VisualDebugMarkerEntityCreator
import com.jafleck.game.entities.entityModules
import com.jafleck.game.gameplay.EngineLogicLoader
import com.jafleck.game.gameplay.gameplayModules
import com.jafleck.game.gameplay.ui.PlayScreen
import com.jafleck.game.maploading.MapLoader
import com.jafleck.game.maploading.mapLoadingModule
import com.jafleck.game.preferences.preferencesModule
import com.jafleck.game.util.asGdxLoggingLevel
import com.jafleck.game.util.files.filesModule
import ktx.app.KtxGame
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

/**
 * Main class of game, delegates different aspects to other modules.
 */
class GdxApplication : KtxGame<Screen>() {

    override fun create() {
        Gdx.app.logLevel = LoggingConfig.gameLoggingLevel.asGdxLoggingLevel()

        val koinApplication = startKoin {
            if (LoggingConfig.koinLoggingLevel != null) {
                printLogger(LoggingConfig.koinLoggingLevel)
            }
            modules(listOf(createMainModule(this@GdxApplication))
                + filesModule
                + preferencesModule
                + entityModules
                + mapLoadingModule
                + assetsModule
                + gameplayModules
            )
        }

        koinApplication.koin.get<GdxHoloSkin>().setAsDefault()
        koinApplication.koin.get<VisualDebugMarkerEntityCreator>() // initialize global debug tool instance

        loadSystems(koinApplication)

        val mapLoader = koinApplication.koin.get<MapLoader>()
        val mapName = "test1.tmx"
//        val mapName = "customized_values_sandbox.tmx"
//        val mapName = "trampoline_preset_test.tmx"
//        val mapName = "single_platform.tmx"
        mapLoader.loadMap(mapName)

        val screen = koinApplication.koin.get<PlayScreen>()
        addScreen(screen)
        setScreen<PlayScreen>()
    }

    private fun loadSystems(koinApplication: KoinApplication) {
        koinApplication.koin.get<EngineLogicLoader>().load(koinApplication.koin.get())
    }

    override fun dispose() {
        super.dispose()
        stopKoin()
    }
}
