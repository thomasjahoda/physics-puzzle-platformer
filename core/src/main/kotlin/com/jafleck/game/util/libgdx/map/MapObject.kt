package com.jafleck.game.util.libgdx.map

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.map.id
import com.jafleck.game.maploading.scaleFromMapToWorld


val MapObject.entityType: String?
    get() = getNullableStringProperty("entityType")

val MapObject.preset: String?
    get() = getNullableStringProperty("preset")

val MapObject.initialVelocity: Vector2?
    get() = getNullableFloatVector2Property("physics_initialVelocity")?.scaleFromMapToWorld().also { verifyNameSetIfCustomized(it) }

val MapObject.customDensity: Float?
    get() = getNullableFloatProperty("physics_customDensity").also { verifyNameSetIfCustomized(it) }

val MapObject.customFriction: Float?
    get() = getNullableFloatProperty("physics_customFriction").also { verifyNameSetIfCustomized(it) }

val MapObject.customRestitution: Float?
    get() = getNullableFloatProperty("physics_customRestitution").also { verifyNameSetIfCustomized(it) }

val MapObject.customLinearDamping: Float?
    get() = getNullableFloatProperty("physics_customLinearDamping").also { verifyNameSetIfCustomized(it) }

val MapObject.customAngularDamping: Float?
    get() = getNullableFloatProperty("physics_customAngularDamping").also { verifyNameSetIfCustomized(it) }

val MapObject.customGravityScale: Float?
    get() = getNullableFloatProperty("physics_customGravityScale").also { verifyNameSetIfCustomized(it) }

val MapObject.customFixedRotation: Boolean?
    get() = getNullableBooleanProperty("physics_customFixedRotation").also { verifyNameSetIfCustomized(it) }

val MapObject.customFillColor: Color?
    get() = (properties["vis_customFillColor"] as Color?).also { verifyNameSetIfCustomized(it) }

val MapObject.customBorderColor: Color?
    get() = (properties["vis_customBorderColor"] as Color?).also { verifyNameSetIfCustomized(it) }

val MapObject.customBorderThickness: Float?
    get() = getNullableFloatProperty("vis_customBorderThickness")?.scaleFromMapToWorld().also { verifyNameSetIfCustomized(it) }


private fun MapObject.getNullableFloatProperty(propertyName: String): Float? {
    val value = properties[propertyName] as Float?
    if (value == null || value == -1f) {
        return null
    }
    return value
}

private fun MapObject.getNullableBooleanProperty(propertyName: String): Boolean? {
    val value = properties[propertyName] as String?
    if (value == null || value == "") {
        return null
    }
    return value.toBoolean()
}

private fun MapObject.getNullableFloatVector2Property(propertyName: String): Vector2? {
    val vectorString = properties[propertyName] as String?
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

private fun MapObject.getNullableStringProperty(propertyName: String): String? {
    val value = properties[propertyName] as String?
    if (value == null || value == "") {
        return null
    }
    return value
}

private fun <T> MapObject.verifyNameSetIfCustomized(value: T?): T? {
    if (value != null) {
        verifyIsNamed()
    }
    return value
}

private fun MapObject.verifyIsNamed() {
    if (name == null) {
        throw RuntimeException("Object with id '$id' must have custom name because it has a custom property that is not a preset selection")
    }
}
