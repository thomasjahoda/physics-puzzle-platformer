package com.jafleck.game.maploading

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

private const val MAP_TO_WORLD_COORDS_SCALAR = 1f / 32f

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
