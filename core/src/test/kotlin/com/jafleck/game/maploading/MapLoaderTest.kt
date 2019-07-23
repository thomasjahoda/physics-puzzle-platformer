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
        val gameMap = GameMap("mapLoaderTest.tmx", "mapLoaderTest.tmx")

        val mapEntitiesLoader = mockk<MapEntitiesLoader>()
        every { mapEntitiesLoader.loadMapEntities(gameMap, any()) } returns Unit

        val uut = MapLoader(CustomClasspathAssetsFileHandleResolver(), mapEntitiesLoader)
        uut.loadMap(gameMap)

        verify { mapEntitiesLoader.loadMapEntities(gameMap, any()) }
    }
}
