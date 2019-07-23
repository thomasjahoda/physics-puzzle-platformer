package com.jafleck.game.maploading

import com.badlogic.ashley.core.Engine
import com.jafleck.extensions.libgdx.map.PatchedTmxMapLoader
import com.jafleck.testutil.CustomClasspathAssetsFileHandleResolver
import com.jafleck.testutil.HeadlessLibgdxExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(HeadlessLibgdxExtension::class)
internal class MapEntitiesLoaderTest {

    @Test
    fun loadMap() {
        val gameMap = GameMap("mapLoaderTest.tmx", "mapLoaderTest.tmx")
        val tiledMap = PatchedTmxMapLoader(CustomClasspathAssetsFileHandleResolver()).load(MapLoader.MAP_ASSETS_DIRECTORY+"/${gameMap.path}")

        val mapEntityLoaderLocator = mockk<MapEntityLoaderLocator>()
        val mockedMapEntityLoader = mockk<MapEntityLoader>()
        every { mapEntityLoaderLocator.getMapEntityLoader("Platform") } returns mockedMapEntityLoader
        every { mapEntityLoaderLocator.getMapEntityLoader("Player") } returns mockedMapEntityLoader
        every { mockedMapEntityLoader.loadEntity(any()) } returns null

        val uut = MapEntitiesLoader(mapEntityLoaderLocator, Engine())
        uut.loadMapEntities(gameMap, tiledMap)

        verify { mapEntityLoaderLocator.getMapEntityLoader("Platform") }
        verify { mapEntityLoaderLocator.getMapEntityLoader("Player") }
        verify { mockedMapEntityLoader.loadEntity(any()) }
    }
}
