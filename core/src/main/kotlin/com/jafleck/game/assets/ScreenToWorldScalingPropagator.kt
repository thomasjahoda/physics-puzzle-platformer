package com.jafleck.game.assets

import com.badlogic.gdx.math.Vector2
import kotlin.properties.Delegates

class ScreenToWorldScalingPropagator {
    private val observers: MutableList<(oldScaling: Vector2, newScaling: Vector2) -> Unit> = arrayListOf()
    var scaling: Vector2 by Delegates.observable(Vector2(1f, 1f)) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            observers.forEach { it(oldValue, newValue) }
        }
    }

    fun registerObserver(observer: (oldScaling: Vector2, newScaling: Vector2) -> Unit) = observers.add(observer)

}
