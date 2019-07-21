package com.jafleck.extensions.libgdx.graphics

import com.badlogic.gdx.graphics.Color


fun Color.mulExceptAlpha(scalar: Float): Color {
    this.r *= scalar
    this.g *= scalar
    this.b *= scalar
    return clamp()
}
