package com.jafleck.extensions.libgdxktx

import com.badlogic.gdx.graphics.Color

fun clearScreen(color: Color) {
    ktx.app.clearScreen(color.r, color.g, color.b, 1f)
}
