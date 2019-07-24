package com.jafleck.game.assets

import com.badlogic.gdx.assets.AssetManager
import com.jafleck.extensions.kotlin.round
import com.jafleck.game.util.files.AssetsFileHandleResolver
import com.jafleck.game.util.logger
import org.koin.dsl.module
import kotlin.system.measureNanoTime

private val assetsLogger = logger("AssetsModule")

val assetsModule = module {
    single {
        AssetManager(get<AssetsFileHandleResolver>()).apply {
            Assets.queueAssetsToLoad(this)
            var loadedAssets = false
            val nanosTakenToLoadAssets = measureNanoTime { loadedAssets = update(10 * 1000) }
            require(loadedAssets) { "Could not load assets within 10 seconds." }
            val millisTakenToLoadAssets = (nanosTakenToLoadAssets / 1000_000f).round(3)
            assetsLogger.info { "Took ${millisTakenToLoadAssets}ms to load assets" }
        }
    }
    single { ScreenToWorldScalingPropagator() }
    single { GdxHoloSkin(get()) }
    single { GameFontsLoader(get(), get()).loadGameFonts() }
}
