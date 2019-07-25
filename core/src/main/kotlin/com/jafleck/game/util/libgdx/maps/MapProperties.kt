package com.jafleck.game.util.libgdx.maps

import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.math.Vector2


fun MapProperties.getNullablePositiveFloatProperty(propertyName: String): Float? {
    val value = this[propertyName] as Float?
    if (value == null || value == -1f) {
        return null
    }
    return value
}

fun MapProperties.getNullablePositiveIntProperty(propertyName: String): Int? {
    val value = this[propertyName] as Int?
    if (value == null || value == -1) {
        return null
    }
    return value
}

fun MapProperties.getNullableBooleanProperty(propertyName: String): Boolean? {
    val value = this[propertyName] as String?
    if (value == null || value == "") {
        return null
    }
    return value.toBoolean()
}

fun MapProperties.getNullableFloatVector2Property(propertyName: String): Vector2? {
    val vectorString = this[propertyName] as String?
    return if (vectorString == null || vectorString == "") {
        null
    } else {

        try {
            val split = vectorString.split(',')
            Vector2(split[0].toFloat(), split[1].toFloat())
        } catch (ex: Exception) {
            throw RuntimeException("Could not parse vector2 string '$vectorString', should have been in format 'float1,float2', e.g. '3.5,10'", ex)
        }
    }
}

fun MapProperties.getNullableStringProperty(propertyName: String): String? {
    val value = this[propertyName] as String?
    if (value == null || value == "") {
        return null
    }
    return value
}
