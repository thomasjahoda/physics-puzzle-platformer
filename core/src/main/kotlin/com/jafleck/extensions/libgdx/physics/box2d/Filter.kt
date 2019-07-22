package com.jafleck.extensions.libgdx.physics.box2d

import com.badlogic.gdx.physics.box2d.Filter

fun Filter.maskAll() {
    maskBits = -1
}
