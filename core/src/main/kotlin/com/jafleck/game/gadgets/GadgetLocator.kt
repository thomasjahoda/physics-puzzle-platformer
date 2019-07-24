package com.jafleck.game.gadgets

import com.jafleck.game.util.koin.ServiceLocatorUtils
import org.koin.core.KoinComponent

class GadgetLocator : KoinComponent {
    private val gadgetsByName = ServiceLocatorUtils.locateServicesOfType<Gadget>(getKoin())
        .associateBy { it.name }

    fun getGadget(name: String): Gadget {
        return gadgetsByName[name] ?: error("Gadget for name '$name' not found. Available names are ${gadgetsByName.keys}.")
    }
}
