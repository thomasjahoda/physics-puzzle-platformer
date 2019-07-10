package com.jafleck.game.entities.creatorutil

import org.koin.dsl.module

val entityCreatorUtilModule = module {
    single { GenericPhysicsBodyCreator(get()) }
}
