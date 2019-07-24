package com.jafleck.game.gadgets

import org.koin.dsl.bind
import org.koin.dsl.module

val gadgetsModule = module {
    // logic around gadgets
    single { GadgetLocator() }
    single { GadgetSelector(get()) }
    // actual gadgets
    single { BallThrowerGadget(get()) } bind Gadget::class
    single { RopeThrowerGadget(get(), get(), get()) } bind Gadget::class
}
