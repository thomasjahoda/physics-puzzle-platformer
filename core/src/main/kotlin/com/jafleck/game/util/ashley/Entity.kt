package com.jafleck.game.util.ashley

import com.badlogic.ashley.core.Entity

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


