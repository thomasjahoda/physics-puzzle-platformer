package com.jafleck.testutil

import com.badlogic.gdx.Files
import com.badlogic.gdx.files.FileHandle
import com.jafleck.game.util.files.AssetsFileHandleResolver

class CustomClasspathAssetsFileHandleResolver : AssetsFileHandleResolver {
    override fun resolve(fileName: String): FileHandle {
        val resource = javaClass.getResource("/$fileName") ?: error("Could not find file $fileName")
        return FileHandle(resource.file)
//        return CustomFileHandle(fileName)
    }

}

private class CustomFileHandle(fileName: String?) : FileHandle(fileName, Files.FileType.Classpath)
