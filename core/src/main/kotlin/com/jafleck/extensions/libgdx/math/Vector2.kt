package com.jafleck.extensions.libgdx.math

import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.kotlin.round

fun Vector2.round(decimals: Int): Vector2 {
    x = x.round(decimals)
    y = y.round(decimals)
    return this
}
