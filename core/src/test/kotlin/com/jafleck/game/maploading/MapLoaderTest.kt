package com.jafleck.game.maploading

import com.jafleck.testutil.CustomClasspathAssetsFileHandleResolver
import com.jafleck.testutil.HeadlessLibgdxExtension
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(HeadlessLibgdxExtension::class)
internal class MapLoaderTest {

    @Test
    fun loadMap() {
        val mapEntityLoaderLocator = mockk<MapEntityLoaderLocator>()
        val mockedMapEntityLoader = mockk<MapEntityLoader>()
        every { mapEntityLoaderLocator.getMapEntityLoader("Platform") } returns mockedMapEntityLoader
        every { mapEntityLoaderLocator.getMapEntityLoader("Player") } returns mockedMapEntityLoader
        every { mockedMapEntityLoader.loadEntity(any()) } returns null

        val mapLoader = MapLoader(CustomClasspathAssetsFileHandleResolver(), mapEntityLoaderLocator)
        mapLoader.loadMap("mapLoaderTest.tmx")

        verify { mapEntityLoaderLocator.getMapEntityLoader("Platform") }
        verify { mapEntityLoaderLocator.getMapEntityLoader("Player") }
        verify { mockedMapEntityLoader.loadEntity(any()) }
    }
}
