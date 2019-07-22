package com.jafleck.game.util.listeners

import com.badlogic.gdx.utils.SnapshotArray
import com.jafleck.extensions.libgdx.utils.safeForEach
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PropertyChangeListenerMultiplexer<T> : PropertyChangeListener<T> {
    override fun invoke(new: T, old: T, property: KProperty<*>) {
        listeners.safeForEach {
            it(new, old, property)
        }
    }

    private val listeners = SnapshotArray<PropertyChangeListener<T>>(1)

    fun addListener(listener: PropertyChangeListener<T>) {
        listeners.add(listener)
    }

    /**
     * @return actual listener registered which can be used to remove the listener again
     */
    fun addNewValueListener(listener: (T) -> Unit): PropertyChangeListener<T> {
        val propertyChangeListener: PropertyChangeListener<T> = { new, _, _ ->
            listener(new)
        }
        addListener(propertyChangeListener)
        return propertyChangeListener
    }

    fun removeListener(listener: PropertyChangeListener<T>) {
        listeners.removeValue(listener, true)
    }
}

fun <T> observableByListeners(initialValue: T, listeners: PropertyChangeListenerMultiplexer<T>):
    ReadWriteProperty<Any?, T> =
    Delegates.observable(initialValue) { property, oldValue, newValue -> listeners(newValue, oldValue, property) }

typealias PropertyChangeListener<T> = (new: T, old: T, property: KProperty<*>) -> Unit
