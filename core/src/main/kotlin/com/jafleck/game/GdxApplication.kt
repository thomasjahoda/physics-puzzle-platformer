package com.jafleck.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.jafleck.game.assets.GdxHoloSkin
import com.jafleck.game.assets.assetsModule
import com.jafleck.game.config.LoggingConfig
import com.jafleck.game.entities.entityModules
import com.jafleck.game.gameplay.EngineLogicLoader
import com.jafleck.game.gameplay.gameplayModule
import com.jafleck.game.gameplay.ui.PlayScreen
import com.jafleck.game.maploading.MapLoader
import com.jafleck.game.maploading.mapLoadingModule
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
                + entityModules
                + mapLoadingModule
                + assetsModule
                + gameplayModule
            )
        }

        koinApplication.koin.get<GdxHoloSkin>().setAsDefault()

        loadSystems(koinApplication)

        val mapLoader = koinApplication.koin.get<MapLoader>()
        mapLoader.loadMap("test1.tmx")

        val screen = koinApplication.koin.get<PlayScreen>()
        addScreen(screen)
        setScreen<PlayScreen>()
    }

    private fun loadSystems(koinApplication: KoinApplication) {
        koinApplication.koin.get<EngineLogicLoader>().load(koinApplication.koin.get())

        // can be used in case the entity systems are defined as separate beans
//        koinApplication.koin.rootScope.beanRegistry.getAllDefinitions().filter {
//            return@filter it.primaryType == EntitySystem::class
//                || it.primaryType.supertypes.map { it.classifier }.contains(EntitySystem::class)
//        }.forEach { it.resolveInstance<EntitySystem>(InstanceContext(koinApplication.koin)) }
    }

    override fun dispose() {
        super.dispose()
        stopKoin()
    }
}
