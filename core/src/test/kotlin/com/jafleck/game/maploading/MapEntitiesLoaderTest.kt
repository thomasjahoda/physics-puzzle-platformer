package com.jafleck.game.maploading

import com.badlogic.ashley.core.Engine
import com.jafleck.game.entities.ActiveGameMapEntity
import com.jafleck.testutil.HeadlessLibgdxExtension
import com.jafleck.testutil.maps.LibGdxTiledMapLoader
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(HeadlessLibgdxExtension::class)
internal class MapEntitiesLoaderTest {

    @Test
    fun loadMap() {
        val gameMap = GameMap("mapLoaderTest.tmx", "mapLoaderTest.tmx")
        val tiledMap = LibGdxTiledMapLoader.loadMap(gameMap)
        val engine = Engine()

        val mapEntityLoaderLocator = mockk<MapEntityLoaderLocator>()
        val mockedMapEntityLoader = mockk<MapEntityLoader>()
        every { mapEntityLoaderLocator.getMapEntityLoader("Platform") } returns mockedMapEntityLoader
        every { mapEntityLoaderLocator.getMapEntityLoader("Player") } returns mockedMapEntityLoader
        every { mockedMapEntityLoader.loadEntity(any()) } returns engine.createEntity()
        val mockedMapDeathBorderLoader = mockk<MapDeathBorderLoader>()
        every { mockedMapDeathBorderLoader.createInvisibleDeathBorder(tiledMap) } returns Unit

        val uut = MapEntitiesLoader(mapEntityLoaderLocator, engine, mockedMapDeathBorderLoader)
        uut.loadMapEntities(gameMap, tiledMap)

        verify { mapEntityLoaderLocator.getMapEntityLoader("Platform") }
        verify { mapEntityLoaderLocator.getMapEntityLoader("Player") }
        verify { mockedMapEntityLoader.loadEntity(any()) }

        Assertions.assertThat(ActiveGameMapEntity.getCurrentMap(engine) != null)
    }
}
