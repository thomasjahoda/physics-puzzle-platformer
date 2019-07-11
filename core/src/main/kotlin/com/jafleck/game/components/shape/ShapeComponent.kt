package com.jafleck.game.components.shape

import com.badlogic.gdx.math.Vector2

interface ShapeComponent {
    fun getRectangleAroundShape(target: Vector2): Vector2
}
