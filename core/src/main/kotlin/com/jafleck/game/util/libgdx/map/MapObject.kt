package com.jafleck.game.util.libgdx.map

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.maploading.scaleFromMapToWorld


val MapObject.velocity: Vector2?
    get() {
        val velocityStr = properties["velocity"] as String?
        return if (velocityStr == null) {
            null
        } else {
            val split = velocityStr.split(',')
            Vector2(split[0].toFloat(), split[1].toFloat()).scaleFromMapToWorld()
        }
    }
