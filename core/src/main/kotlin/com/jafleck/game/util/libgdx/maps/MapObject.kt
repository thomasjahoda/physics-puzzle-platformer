package com.jafleck.game.util.libgdx.maps

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.maps.id
import com.jafleck.game.maploading.scaleFromMapToWorld


val MapObject.entityType: String?
    get() = properties.getNullableStringProperty("entityType")

val MapObject.preset: String?
    get() = properties.getNullableStringProperty("preset")

val MapObject.initialVelocity: Vector2?
    get() = properties.getNullableFloatVector2Property("physics_initialVelocity")?.scaleFromMapToWorld().also { verifyNameSetIfCustomized(it) }

val MapObject.customDensity: Float?
    get() = properties.getNullablePositiveFloatProperty("physics_customDensity").also { verifyNameSetIfCustomized(it) }

val MapObject.customFriction: Float?
    get() = properties.getNullablePositiveFloatProperty("physics_customFriction").also { verifyNameSetIfCustomized(it) }

val MapObject.customRestitution: Float?
    get() = properties.getNullablePositiveFloatProperty("physics_customRestitution").also { verifyNameSetIfCustomized(it) }

val MapObject.customLinearDamping: Float?
    get() = properties.getNullablePositiveFloatProperty("physics_customLinearDamping").also { verifyNameSetIfCustomized(it) }

val MapObject.customAngularDamping: Float?
    get() = properties.getNullablePositiveFloatProperty("physics_customAngularDamping").also { verifyNameSetIfCustomized(it) }

val MapObject.customGravityScale: Float?
    get() = properties.getNullablePositiveFloatProperty("physics_customGravityScale").also { verifyNameSetIfCustomized(it) }

val MapObject.customFixedRotation: Boolean?
    get() = properties.getNullableBooleanProperty("physics_customFixedRotation").also { verifyNameSetIfCustomized(it) }

val MapObject.customFillColor: Color?
    get() = (properties["vis_customFillColor"] as Color?).also { verifyNameSetIfCustomized(it) }

val MapObject.customBorderColor: Color?
    get() = (properties["vis_customBorderColor"] as Color?).also { verifyNameSetIfCustomized(it) }

val MapObject.customBorderThickness: Float?
    get() = properties.getNullablePositiveFloatProperty("vis_customBorderThickness")?.scaleFromMapToWorld().also { verifyNameSetIfCustomized(it) }

val MapObject.layerIndex: Int?
    get() = properties.getNullablePositiveIntProperty("layerIndex")

fun <T> MapObject.verifyNameSetIfCustomized(value: T?): T? {
    if (value != null) {
        verifyIsNamed()
    }
    return value
}

fun MapObject.verifyIsNamed() {
    if (name == null) {
        throw RuntimeException("Object with id '$id' must have custom name because it has a custom property that is not a preset selection")
    }
}
