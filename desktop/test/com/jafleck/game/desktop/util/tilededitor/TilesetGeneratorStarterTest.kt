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
        val targetPngImageFile = File.createTempFile("atlas-terrain", ".png")
        val targetTilesetXmlFile = File.createTempFile("atlas-terrain", ".tsx")
        // uncomment if targeting actual files
//        val targetPngImageFile = File("../android/assets/maps/atlas-tileset.png")
//        val targetTilesetXmlFile = File("../android/assets/maps/atlas-tileset.tsx")

        val atlas = AssetDescriptor("../android/assets/atlas/textures.atlas", TextureAtlas::class.java)

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
