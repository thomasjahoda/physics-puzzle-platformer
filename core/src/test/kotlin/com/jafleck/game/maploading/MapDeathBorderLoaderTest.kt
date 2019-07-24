package com.jafleck.game.maploading

import com.jafleck.game.entities.DeathZoneEntityCreator
import com.jafleck.testutil.HeadlessLibgdxExtension
import com.jafleck.testutil.maps.LibGdxTiledMapLoader
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(HeadlessLibgdxExtension::class)
internal class MapDeathBorderLoaderTest {

    @Test
    fun loadMap() {
        val gameMap = GameMap("mapLoaderTest.tmx", "mapLoaderTest.tmx")
        val tiledMap = LibGdxTiledMapLoader.loadMap(gameMap)

        val deathZoneEntityCreator = mockk<DeathZoneEntityCreator>(relaxed = true)
//        every { deathZoneEntityCreator.createInvisibleDeathZone(any()) } returns DeathZoneEntity(Entity()) // https://github.com/mockk/mockk/issues/152

        val uut = MapDeathBorderLoader(deathZoneEntityCreator)
        uut.createInvisibleDeathBorder(tiledMap)

        verify(exactly = 4) { deathZoneEntityCreator.createInvisibleDeathZone(any()) }
    }
}
