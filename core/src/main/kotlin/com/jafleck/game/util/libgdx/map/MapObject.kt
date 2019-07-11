package com.jafleck.game.util.libgdx.map

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.maploading.scaleFromMapToWorld


val MapObject.initialVelocity: Vector2?
    get() {
        val velocityStr = properties["physics_initialVelocity"] as String?
        return if (velocityStr == null) {
            null
        } else {
            try {
                val split = velocityStr.split(',')
                Vector2(split[0].toFloat(), split[1].toFloat()).scaleFromMapToWorld()
            } catch (ex: Exception) {
                throw RuntimeException("Could not parse initial velocity string '$velocityStr', should have been in format 'float1,float2', e.g. '3.5,10'", ex)
            }
        }
    }

val MapObject.customFillColor: Color?
    get() {
        return properties["vis_customFillColor"] as Color?
    }


val MapObject.customBorderColor: Color?
    get() {
        return properties["vis_customBorderColor"] as Color?
    }

val MapObject.customBorderThickness: Float?
    get() {
        val customBorderThickness = properties["vis_customBorderThickness"] as Float?
        if (customBorderThickness == null || customBorderThickness == -1f) {
            return null
        }
        return customBorderThickness.scaleFromMapToWorld()
    }
