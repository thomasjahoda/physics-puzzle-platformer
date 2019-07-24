package com.jafleck.game.util.koin

import org.koin.core.Koin
import org.koin.core.instance.InstanceContext

object ServiceLocatorUtils {

    /**
     * Services must use 'bind T::class' to specify a secondary type that can be resolved.
     */
    inline fun <reified T> locateServicesOfType(koin: Koin): List<T> {
        return koin.rootScope.beanRegistry.getDefinitionsForClass(T::class)
            .map { it.resolveInstance<T>(InstanceContext(koin)) }
    }

}
