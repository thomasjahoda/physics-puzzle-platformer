package com.jafleck.game.desktop.util.tilededitor

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File

internal class TilesetGeneratorStarterTest {

    @Disabled("manual test - takes too long and also depends on some project files")
    @Test
    fun generateFromAtlas() {
//        val targetPngImageFile = File("../core/main/src/resources/maps/atlas-tileset.png")
//        val targetTilesetXmlFile = File("../core/main/src/resources/maps/atlas-tileset.tsx")

        // uncomment for targeting temporary files
        val targetPngImageFile = File.createTempFile("atlas-terrain", ".png")
        val targetTilesetXmlFile = File.createTempFile("atlas-terrain", ".tsx")

        val atlas = AssetDescriptor("../core/src/main/resources/atlas/textures.atlas", TextureAtlas::class.java)

        TerrainGeneratorStarter.useTilesetGenerator {
            it.generateFromAtlas(atlas, 32,
                targetPngImageFile,
                targetTilesetXmlFile
            )
        }
        Assertions.assertThat(targetPngImageFile.exists()).isTrue()
        Assertions.assertThat(targetTilesetXmlFile.exists()).isTrue()
    }
}
