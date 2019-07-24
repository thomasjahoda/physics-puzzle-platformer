package com.jafleck.game.maploading

import org.koin.dsl.module

val mapLoadingModule = module {
    single { MapEntityLoaderLocator() }
    single { MapLoader(get(), get()) }
    single { MapEntitiesLoader(get(), get(), get()) }
    single { MapDeathBorderLoader(get()) }
    single { MapUnloader(get(), get()) }
    single { GameMapList(get()) }
    single { MapSelection(get(), get(), get(), get()) }
}
