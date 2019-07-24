package com.jafleck.game.maploading

import com.jafleck.game.util.koin.ServiceLocatorUtils
import org.koin.core.KoinComponent
import org.koin.core.instance.InstanceContext


class MapEntityLoaderLocator : KoinComponent {

    private val entityMapLoadersByType = ServiceLocatorUtils.locateServicesOfType<MapEntityLoader>(getKoin())
        .associateBy { it.type }

    fun getMapEntityLoader(mapObjectType: String): MapEntityLoader {
        return entityMapLoadersByType[mapObjectType] ?: error("Map entity loader for type '$mapObjectType' not found")
    }
}
