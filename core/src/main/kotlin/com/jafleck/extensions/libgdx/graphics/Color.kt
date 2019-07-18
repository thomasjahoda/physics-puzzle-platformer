package com.jafleck.extensions.libgdx.graphics

import com.badlogic.gdx.graphics.Color


fun Color.cpyWithAlpha(alpha: Float): Color {
    return this.cpy().apply {
        a = alpha
    }
}


fun Color.mulExceptAlpha(scalar: Float): Color {
    this.r *= scalar
    this.g *= scalar
    this.b *= scalar
    return clamp()
}
