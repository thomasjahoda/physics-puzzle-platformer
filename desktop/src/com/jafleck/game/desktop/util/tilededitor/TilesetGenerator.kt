package com.jafleck.game.desktop.util.tilededitor

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import org.redundent.kotlin.xml.Node
import org.redundent.kotlin.xml.node
import org.redundent.kotlin.xml.xml
import java.io.File

/**
 * Generates tileset inclusive terrains for the Tiled map editor.
 *
 * Sorry, this code is bad.
 * This utility requires the Gdx files and graphics instances to exist, so it is just really, really bad.
 * I start an LwjglApplication (inclusive Window) just to execute this utility, I'm so sorry.
 */
class TerrainGenerator {
    fun generateFromAtlas(atlas: AssetDescriptor<TextureAtlas>, cellSize: Int, targetPngImageFile: File, targetTilesetXmlFile: File) {
        val namedNinePatches = getNinePatches(atlas)

        // some constants for rendering
        val terrainColumns = 3
        val terrainRows = 4
        val terrainImageWidth = cellSize * terrainColumns
        val terrainImageHeight = cellSize * terrainRows
        val totalImageWidth = terrainImageWidth * namedNinePatches.size
        val totalImageHeight = terrainImageHeight
        val cellSizeF = cellSize.toFloat()
        val totalColumns = namedNinePatches.size * terrainColumns
//        val totalRows = terrainRows // unused

        fun getTileId(tileX: Int, tileY: Int): Int {
            return tileY * totalColumns + tileX
        }

        // basic data for tileset
        val tilesetXml = xml("tileset") {
            attribute("version", "1.2")
            attribute("tiledversion", "1.2.3")
            attribute("name", targetTilesetXmlFile.name)
            attribute("tilewidth", cellSize.toString())
            attribute("tileheight", cellSize.toString())
            attribute("tilecount", (namedNinePatches.size * 3 * 4).toString())
            attribute("columns", totalColumns.toString())

            "image" {
                attribute("source", targetPngImageFile.relativeTo(targetTilesetXmlFile.parentFile).path)
                attribute("width", totalImageWidth.toString())
                attribute("height", totalImageHeight.toString())
            }
        }

        val terrainTypesNode = node("terraintypes").apply { tilesetXml.addNode(this) }

        // rendering tileset
        val fbo = FrameBuffer(Pixmap.Format.RGBA8888, totalImageWidth, totalImageHeight, true, true)
        try {
            fbo.begin()
            val spriteBatch = SpriteBatch()
            spriteBatch.projectionMatrix = ScreenViewport().apply {
                update(totalImageWidth, totalImageHeight, true)
            }.camera.combined
            spriteBatch.begin()
            namedNinePatches.forEachIndexed { ninePatchIndex, namedNinePatch ->
                val terrainIdentifierFromTiles = ninePatchIndex.toString()
                terrainTypesNode.addNode(node("terrain") {
                    attribute("name", namedNinePatch.name)
                    attribute("tile", terrainIdentifierFromTiles)
                })

                /*
                 * This will draw the following cells (from top-left, as seen by 'Tiled' editor):
                 * [all-in-one-cell]    [all-in-one-cell]      [empty]
                 * [top-left corner]    [top-side-only]        [top-right corner]
                 * [left-side only]     [stretched space only] [right-side only]
                 * [bottom-left corner] [bottom-side-only]     [bottom-right corner]
                 */
                val imageX = (ninePatchIndex * terrainImageWidth).toFloat()
                val imageY = 0f
                namedNinePatch.patch.draw(spriteBatch, imageX + (0 * cellSizeF), imageY + (0 * cellSizeF), cellSizeF, cellSizeF)
                namedNinePatch.patch.draw(spriteBatch, imageX + (0 * cellSizeF), imageY + (1 * cellSizeF), 3 * cellSizeF, 3 * cellSizeF)

                fun addTileNode(terrainTileCategory: TerrainTileCategory) {
                    tilesetXml.addNode(node("tile") {
                        attribute("id", getTileId(ninePatchIndex * terrainColumns + terrainTileCategory.relativePositionX, terrainTileCategory.relativePositionY))
                        attribute("type", namedNinePatch.name + (if (terrainTileCategory.typePostfix != null) "-${terrainTileCategory.typePostfix}" else ""))
                        if (terrainTileCategory.hasCorners()) {
                            attribute("terrain", terrainTileCategory.getTiledMapEditorTerrainCornersString(terrainIdentifierFromTiles))
                        }
                    })
                }

                TerrainTileCategory.values().forEach { addTileNode(it) }
            }

            // TODO add sprites other than nine-patches

            spriteBatch.end()
            writeCurrentFrameBufferToFileAsPng(targetPngImageFile, totalImageWidth, totalImageHeight)
        } finally {
            fbo.end()
            fbo.dispose()
        }

        writeXmlTo(tilesetXml, targetTilesetXmlFile)
    }

    private fun writeXmlTo(tilesetXml: Node, targetTilesetXmlFile: File) {
        val xmlString = tilesetXml.toString(true)
        targetTilesetXmlFile.createNewFile()
        targetTilesetXmlFile.writeText(xmlString)
    }

    private fun getNinePatches(atlas: AssetDescriptor<TextureAtlas>): List<NamedNinePatchHolder> {
        val assetManager = AssetManager().apply {
            load(atlas)
            if (!update(10_000)) {
                error("loading assets took too long")
            }
        }

        val textureAtlas = assetManager.get(atlas)
        val ninePatchRegions = textureAtlas.regions
            .filter { it.splits != null }
        return ninePatchRegions.map {
            NamedNinePatchHolder(it.name, textureAtlas.createPatch(it.name))
        }
    }

    private fun writeCurrentFrameBufferToFileAsPng(targetFile: File, width: Int, height: Int) {
        val pixels = ScreenUtils.getFrameBufferPixels(0, 0, width, height, true)
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        BufferUtils.copy(pixels, 0, pixmap.pixels, pixels.size)
        PixmapIO.writePNG(FileHandle(targetFile), pixmap)
        pixmap.dispose()
    }
}

private data class NamedNinePatchHolder(val name: String,
                                        val patch: NinePatch)

private enum class TerrainTileCategory(val relativePositionX: Int, val relativePositionY: Int, val typePostfix: String?, private val selectedTerrainCorners: Set<Corner>) {
    TOP_LEFT_CORNER(0, 0, "topleft", setOf(Corner.BOTTOM_RIGHT)),
    TOP_EDGE(1, 0, "top", setOf(Corner.BOTTOM_RIGHT, Corner.BOTTOM_LEFT)),
    TOP_RIGHT_CORNER(2, 0, "topright", setOf(Corner.BOTTOM_LEFT)),
    LEFT_EDGE(0, 1, "left", setOf(Corner.BOTTOM_RIGHT, Corner.TOP_RIGHT)),
    MIDDLE(1, 1, "middle", setOf(Corner.BOTTOM_RIGHT, Corner.BOTTOM_LEFT, Corner.TOP_RIGHT, Corner.TOP_LEFT)),
    RIGHT_EDGE(2, 1, "right", setOf(Corner.BOTTOM_LEFT, Corner.TOP_LEFT)),
    BOTTOM_LEFT_CORNER(0, 2, "bottomleft", setOf(Corner.TOP_RIGHT)),
    BOTTOM_EDGE(1, 2, "bottom", setOf(Corner.TOP_RIGHT, Corner.TOP_LEFT)),
    BOTTOM_RIGHT_CORNER(2, 2, "bottomright", setOf(Corner.TOP_LEFT)),
    ALL_IN_ONE(0, 3, null, setOf());

    fun hasCorners(): Boolean {
        return selectedTerrainCorners.isNotEmpty()
    }

    fun getTiledMapEditorTerrainCornersString(terrainIdentifierFromTiles: String): String {
        return Corner.values().joinToString(separator = ",") { corner ->
            if (selectedTerrainCorners.contains(corner)) {
                terrainIdentifierFromTiles
            } else {
                ""
            }
        }
    }
}

private enum class Corner {
    TOP_LEFT, TOP_RIGHT,
    BOTTOM_LEFT, BOTTOM_RIGHT
}
