package com.jafleck.game.entities.creatorutil

import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.entities.config.GenericEntityConfig

class VisualShapeCreator {

    fun createVisualShape(config: GenericEntityConfig): VisualShapeComponent {
        val visualShapeComponent = VisualShapeComponent(null, null, null)
        customizeVisualShape(visualShapeComponent, config)
        return visualShapeComponent
    }

    fun createVisualShapeIfColorIsSet(config: GenericEntityConfig): VisualShapeComponent? {
        return if (config.fillColor != null) {
            createVisualShape(config)
        } else {
            null
        }
    }

    fun customizeVisualShape(visualShapeComponent: VisualShapeComponent,
                             config: GenericEntityConfig) {
        withItIfNotNull(config.fillColor) {
            visualShapeComponent.fillColor = it.cpy()
        }
        withItIfNotNull(config.borderColor) {
            visualShapeComponent.borderColor = it.cpy()
        }
        withItIfNotNull(config.borderThickness) {
            visualShapeComponent.borderThickness = it
        }
        withItIfNotNull(config.visualLayerIndex) {
            visualShapeComponent.visualLayerIndex = it
        }
    }
}

fun VisualShapeComponent.apply(genericEntityConfig: GenericEntityConfig, visualShapeCreator: VisualShapeCreator): VisualShapeComponent {
    visualShapeCreator.customizeVisualShape(this, genericEntityConfig)
    return this
}
