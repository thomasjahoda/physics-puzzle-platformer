package com.jafleck.game.maploading

import org.koin.dsl.module

val mapLoadingModule = module {
    single { MapEntityLoaderLocator() }
    single { MapLoader(get(), get(), get()) }
    single { MapUnloader(get(), get()) }
    single { MapList(get()) }
    single { MapSelection(get(), get()) }
}
