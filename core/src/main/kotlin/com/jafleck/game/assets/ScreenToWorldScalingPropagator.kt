package com.jafleck.game.assets

import com.badlogic.gdx.math.Vector2
import com.jafleck.game.util.listeners.PropertyChangeListenerMultiplexer
import kotlin.properties.Delegates

class ScreenToWorldScalingPropagator {
    val worldToScreenScalingFactorListeners = PropertyChangeListenerMultiplexer<Vector2>()
    var worldToScreenScalingFactor: Vector2 by Delegates.observable(Vector2(1f, 1f)) { property, oldValue, newValue ->
        if (oldValue != newValue) {
            worldToScreenScalingFactorListeners(newValue, oldValue, property)
        }
    }
}
