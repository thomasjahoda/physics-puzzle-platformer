package com.jafleck.game.entities.maploading

import com.badlogic.gdx.maps.MapObject
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdx.map.id
import com.jafleck.game.components.VisualShapeComponent
import com.jafleck.game.util.libgdx.map.customBorderColor
import com.jafleck.game.util.libgdx.map.customBorderThickness
import com.jafleck.game.util.libgdx.map.customFillColor
import com.jafleck.game.util.logger

class CustomizeVisualShapeLoader {

    private val logger = logger(this::class)

    fun customizeVisualShape(mapObject: MapObject,
                             visualShapeComponent: VisualShapeComponent) {
        logger.debug { "Parsing map object ${mapObject.id}" }

        withItIfNotNull(mapObject.customFillColor) {
            visualShapeComponent.fillColor = it.cpy()
        }
        withItIfNotNull(mapObject.customBorderColor) {
            visualShapeComponent.borderColor = it.cpy()
        }
        withItIfNotNull(mapObject.customBorderThickness) {
            visualShapeComponent.borderThickness = it
        }
    }
}

fun VisualShapeComponent.customizeBy(mapObject: MapObject, customizeVisualShapeLoader: CustomizeVisualShapeLoader): VisualShapeComponent {
    customizeVisualShapeLoader.customizeVisualShape(mapObject, this)
    return this
}
