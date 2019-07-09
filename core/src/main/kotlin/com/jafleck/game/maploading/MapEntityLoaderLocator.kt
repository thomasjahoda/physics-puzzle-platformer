package com.jafleck.game.maploading

import org.koin.core.KoinComponent
import org.koin.core.instance.InstanceContext


class MapEntityLoaderLocator : KoinComponent {

    private val entityMapLoadersByType = getKoin().rootScope.beanRegistry.getAllDefinitions()
        .filter { beanDefinition ->
            return@filter beanDefinition.primaryType == MapEntityLoader::class
                || beanDefinition.primaryType.supertypes.find { it.classifier == MapEntityLoader::class } != null
        }
        .map { it.resolveInstance<MapEntityLoader>(InstanceContext(getKoin())) }
        .associateBy { it.type }

    fun getMapEntityLoader(mapObjectType: String): MapEntityLoader {
        return entityMapLoadersByType[mapObjectType] ?: error("Map entity loader for type '$mapObjectType' not found")
    }
}
