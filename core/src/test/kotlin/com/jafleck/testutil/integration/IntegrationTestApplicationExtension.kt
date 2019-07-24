package com.jafleck.testutil.integration

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.game.assets.GameFonts
import com.jafleck.game.assets.GdxHoloSkin
import com.jafleck.game.assets.assetsModule
import com.jafleck.game.config.LoggingConfig
import com.jafleck.game.entities.PlayerEntity
import com.jafleck.game.entities.RopeEntityCreatorTest
import com.jafleck.game.entities.VisualDebugMarkerEntityCreator
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.entityModules
import com.jafleck.game.gadgets.gadgetsModule
import com.jafleck.game.gameplay.*
import com.jafleck.game.maploading.GameMap
import com.jafleck.game.maploading.MapLoader
import com.jafleck.game.maploading.mapLoadingModule
import com.jafleck.game.util.LoggingLevel
import com.jafleck.game.util.asGdxLoggingLevel
import com.jafleck.testutil.preferences.testPreferencesModule
import io.mockk.mockk
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

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
                        utilitiesModule,
                        gadgetsModule,
                        entityComponentSystemBasicsModule,
                        entityComponentSystemLogicModule,
                        controlAndMainPhasesModule,
                        physicsModule
                    )
                modules = if (loadAssets) {
                    // TODO cache assets over multiple tests if this is ever necessary
                    modules + assetsModule
                } else {
                    modules + module {
                        single {
                            GameFonts(
                                `bold 0_5f world size font` = mockk(),
                                `bold 1f world size font` = mockk()
                            )
                        }
                    }
                }
                modules
            }())
        }
        this.koinApplication = koinApplication

        val koin = koinApplication.koin
        if (loadAssets) {
            koin.get<GdxHoloSkin>().setAsDefault()
        }
        koin.get<VisualDebugMarkerEntityCreator>() // initialize global debug tool instance

        if (loadedMap != null) {
            val mapLoader = koin.get<MapLoader>()
            mapLoader.loadMap(GameMap(loadedMap, loadedMap))
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
