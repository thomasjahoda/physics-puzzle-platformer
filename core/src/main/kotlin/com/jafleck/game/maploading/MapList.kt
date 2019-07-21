package com.jafleck.game.maploading

import com.beust.klaxon.Klaxon
import com.jafleck.game.util.files.AssetsFileHandleResolver
import com.jafleck.game.util.logger
import java.io.File

class MapList(
    private val assetsFileHandleResolver: AssetsFileHandleResolver
) {
    private val logger = logger(this::class)

    val maps: List<Map>

    init {
        val mapListFilePath = "maps/list.json"
        val file = assetsFileHandleResolver.resolve(mapListFilePath)
        logger.debug { "Reading $mapListFilePath" }
        val jsonString = file.readString(Charsets.UTF_8.name())
        maps = Klaxon().parseArray<SerializedMap>(jsonString)!!
            .map { Map(File(it.path).name, it.path) }
            .sortedBy { it.path }
    }
}

private data class SerializedMap(val path: String)
data class Map(val name: String, val path: String)
