package com.jafleck.game.entities.maploading

import org.koin.dsl.module

val entityMaploadingModule = module {
    single { MapObjectFormExtractor() }
    single { CustomizeVisualShapeLoader() }
}
