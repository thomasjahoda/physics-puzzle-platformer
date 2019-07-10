package com.jafleck.game.maploading

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

private const val WORLD_TO_MAP_COORDS_SCALAR = 32f
private const val MAP_TO_WORLD_COORDS_SCALAR = 1f / WORLD_TO_MAP_COORDS_SCALAR

fun Float.scaleFromMapToWorld(): Float {
    return this * MAP_TO_WORLD_COORDS_SCALAR
}

fun Vector2.scaleFromMapToWorld(): Vector2 {
    this.x *= MAP_TO_WORLD_COORDS_SCALAR
    this.y *= MAP_TO_WORLD_COORDS_SCALAR
    return this
}

fun Float.scaleFromWorldToMap(): Float {
    return this * WORLD_TO_MAP_COORDS_SCALAR
}

fun Vector2.scaleFromWorldToMap(): Vector2 {
    this.x *= WORLD_TO_MAP_COORDS_SCALAR
    this.y *= WORLD_TO_MAP_COORDS_SCALAR
    return this
}

fun getRectangleWorldCoordinates(rectangleMapObject: RectangleMapObject, targetRectangle: Rectangle = Rectangle()): Rectangle {
    return getRectangleWorldCoordinates(rectangleMapObject.rectangle, targetRectangle)
}

fun getRectangleWorldCoordinates(source: Rectangle, target: Rectangle = Rectangle()): Rectangle {
    target.x = source.x * MAP_TO_WORLD_COORDS_SCALAR
    target.y = source.y * MAP_TO_WORLD_COORDS_SCALAR
    target.width = source.width * MAP_TO_WORLD_COORDS_SCALAR
    target.height = source.height * MAP_TO_WORLD_COORDS_SCALAR
    return target
}

fun mapToWorldCoordinates(source: Vector2, target: Vector2 = Vector2()): Vector2 {
    target.x = source.x * MAP_TO_WORLD_COORDS_SCALAR
    target.y = source.y * MAP_TO_WORLD_COORDS_SCALAR
    return target
}
