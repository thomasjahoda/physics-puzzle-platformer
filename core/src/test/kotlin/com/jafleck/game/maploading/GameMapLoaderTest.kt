package com.jafleck.game.maploading

import com.badlogic.ashley.core.Engine
import com.jafleck.testutil.CustomClasspathAssetsFileHandleResolver
import com.jafleck.testutil.HeadlessLibgdxExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(HeadlessLibgdxExtension::class)
internal class GameMapLoaderTest {

    @Test
    fun loadMap() {
        val mapEntityLoaderLocator = mockk<MapEntityLoaderLocator>()
        val mockedMapEntityLoader = mockk<MapEntityLoader>()
        every { mapEntityLoaderLocator.getMapEntityLoader("Platform") } returns mockedMapEntityLoader
        every { mapEntityLoaderLocator.getMapEntityLoader("Player") } returns mockedMapEntityLoader
        every { mockedMapEntityLoader.loadEntity(any()) } returns null

        val mapLoader = MapLoader(CustomClasspathAssetsFileHandleResolver(), mapEntityLoaderLocator, Engine())
        mapLoader.loadMap(GameMap("mapLoaderTest.tmx", "mapLoaderTest.tmx"))

        verify { mapEntityLoaderLocator.getMapEntityLoader("Platform") }
        verify { mapEntityLoaderLocator.getMapEntityLoader("Player") }
        verify { mockedMapEntityLoader.loadEntity(any()) }
    }
}
