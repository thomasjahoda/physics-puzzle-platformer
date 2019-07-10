package com.jafleck.extensions.libgdxktx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20

fun clearScreen(color: Color) {
    // see https://stackoverflow.com/questions/35969253/libgdx-antialiasing for antialising, I hope I am doing this right
    ktx.app.clearScreen(color.r, color.g, color.b, 1f)
    Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT or (if (Gdx.graphics.bufferFormat.coverageSampling) GL20.GL_COVERAGE_BUFFER_BIT_NV else 0))
}
