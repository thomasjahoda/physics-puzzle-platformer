package com.jafleck.game.entities.creatorutil

import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.game.components.visual.VisualShapeComponent
import com.jafleck.game.entities.customizations.GenericEntityCustomization

class VisualShapeCreator {

    fun createVisualShape(customization: GenericEntityCustomization): VisualShapeComponent {
        val visualShapeComponent = VisualShapeComponent(null, null, null)
        customizeVisualShape(visualShapeComponent, customization)
        return visualShapeComponent
    }

    fun customizeVisualShape(visualShapeComponent: VisualShapeComponent,
                             customization: GenericEntityCustomization) {
        withItIfNotNull(customization.fillColor) {
            visualShapeComponent.fillColor = it.cpy()
        }
        withItIfNotNull(customization.borderColor) {
            visualShapeComponent.borderColor = it.cpy()
        }
        withItIfNotNull(customization.borderThickness) {
            visualShapeComponent.borderThickness = it
        }
    }
}

fun VisualShapeComponent.apply(genericEntityCustomization: GenericEntityCustomization, visualShapeCreator: VisualShapeCreator): VisualShapeComponent {
    visualShapeCreator.customizeVisualShape(this, genericEntityCustomization)
    return this
}
