package com.jafleck.extensions.kotlin

inline fun <T> applyIfNotNull(receiver: T?, block: T.() -> Unit): Boolean {
    return if (receiver == null) {
        false
    } else {
        receiver.block()
        true
    }
}

inline fun <T> withItIfNotNull(it: T?, block: (T) -> Unit): Boolean {
    return if (it == null) {
        false
    } else {
        block(it)
        true
    }
}
