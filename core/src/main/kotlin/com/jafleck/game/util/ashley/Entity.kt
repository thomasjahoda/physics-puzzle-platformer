package com.jafleck.game.util.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.shape.RectangleShapeComponent

fun Entity.getComponentsDebugDump(): String {
    return components.joinToString(separator = "\n") {
        it.toString()
    }
}

fun Entity.getDebugDump(): String {
    return "Entity: ${this}\n" +
        "============================\n" +
        "${this.getComponentsDebugDump()}\n" +
        "============================"
}


fun Entity.addIfNotNull(component: Component?) {
    if (component != null) {
        add(component)
    }
}

fun Entity.createPositionAndForm(rectangle: Rectangle) {
    add(OriginPositionComponent(rectangle.getCenter(Vector2())))
    add(RectangleShapeComponent(rectangle.width, rectangle.height))
}
