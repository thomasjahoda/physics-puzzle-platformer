package com.jafleck.extensions.kotlin

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class ReadWritePropertyDelegate<T>(val initializer: () -> T) : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null) {
            // TODO synchronized?
            value = initializer()
        }
        return value!!
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }
}

fun <T> lazyReadWrite(initializer: () -> T): ReadWriteProperty<Any?, T> = ReadWritePropertyDelegate(initializer)
