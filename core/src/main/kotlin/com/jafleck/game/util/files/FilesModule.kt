package com.jafleck.game.util.files

import org.koin.dsl.module

val filesModule = module {
    single<AssetsFileHandleResolver> { DefaultAssetsFileHandleResolver() }
}
