package com.jafleck.game

import org.koin.core.module.Module
import org.koin.dsl.module

fun createMainModule(gdxApplication: GdxApplication): Module {
    return module {
        single { gdxApplication }
    }
}
