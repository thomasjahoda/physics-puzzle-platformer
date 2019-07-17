package com.jafleck.extensions.libgdx.utils

import com.badlogic.gdx.utils.SnapshotArray

fun <T> SnapshotArray<T>.safeForEach(action: (T) -> Unit) {
    begin()
    for (element in this) action(element)
    end()
}
