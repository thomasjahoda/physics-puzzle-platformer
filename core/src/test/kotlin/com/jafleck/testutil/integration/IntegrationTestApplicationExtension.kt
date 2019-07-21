package com.jafleck.testutil.integration

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.game.assets.GdxHoloSkin
import com.jafleck.game.assets.assetsModule
import com.jafleck.game.config.LoggingConfig
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.entities.RopeEntityCreatorTest
import com.jafleck.game.entities.VisualDebugMarkerEntityCreator
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.entityModules
import com.jafleck.game.gameplay.*
import com.jafleck.game.maploading.MapLoader
import com.jafleck.game.maploading.mapLoadingModule
import com.jafleck.game.util.LoggingLevel
import com.jafleck.game.util.asGdxLoggingLevel
import com.jafleck.testutil.preferences.testPreferencesModule
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class IntegrationTestApplicationExtension(
    private val loadAssets: Boolean = false,
    private val loadedMap: String? = null
) : BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private var koinApplication: KoinApplication? = null

    override fun beforeTestExecution(context: ExtensionContext) {
        loadLimitedApplicationContext()
    }

    override fun afterTestExecution(context: ExtensionContext) {
        stopKoin()
        koinApplication = null
    }

    private fun loadLimitedApplicationContext() {
        Gdx.app.logLevel = LoggingLevel.DEBUG.asGdxLoggingLevel() // ignoring config

        val koinApplication = startKoin {
            if (LoggingConfig.koinLoggingLevel != null) {
                printLogger(LoggingConfig.koinLoggingLevel!!)
            }
            modules({
                var modules = listOf(testFilesModule) +
                    testPreferencesModule +
                    entityModules +
                    mapLoadingModule +
                    listOf(
                        gameplayUtilitiesModule,
                        gameplayGadgetsModule,
                        gameplayEntityComponentSystemBasicsModule,
                        gameplayEntityComponentSystemLogicModule,
                        gameplayPhysicsModule
                    )
                if (loadAssets) {
                    // TODO cache assets over multiple tests
                    modules = modules + assetsModule
                }
                modules
            }())
        }
        this.koinApplication = koinApplication

        if (loadAssets) {
            koinApplication.koin.get<GdxHoloSkin>().setAsDefault()
        }
        koinApplication.koin.get<VisualDebugMarkerEntityCreator>() // initialize global debug tool instance

        if (loadedMap != null) {
            val mapLoader = koinApplication.koin.get<MapLoader>()
            mapLoader.loadMap(loadedMap)
        }
    }

    val koin
        get() = this.koinApplication!!.koin

    val engine
        get() = koin.get<Engine>()
    val world
        get() = koin.get<World>()
    val genericPhysicsBodyCreator
        get() = koin.get<GenericPhysicsBodyCreator>()

    val player
        get() = PlayerEntity(RopeEntityCreatorTest.app.engine.getEntitiesFor(PlayerEntity.family)[0])
}
