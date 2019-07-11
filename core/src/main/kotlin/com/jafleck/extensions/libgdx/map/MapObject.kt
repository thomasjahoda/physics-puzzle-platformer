package com.jafleck.extensions.libgdx.map

import com.badlogic.gdx.maps.MapObject

val MapObject.id
    get() = properties["id"] as Int

val MapObject.rotationDegrees: Float?
    get() {
        val rotationDegrees = properties["rotation"] as Float?
        return if (rotationDegrees == null) {
            null
        } else {
            // rotation in tiled rotates clockwise starting from pointing to the right even though the standard-rotation rotates counter-clockwise
            // convert to counter-clockwise:
            (360f - rotationDegrees).rem(360f)
        }
    }
