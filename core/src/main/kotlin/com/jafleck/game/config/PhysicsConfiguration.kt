package com.jafleck.game.config

object PhysicsConfiguration {
    val debugRendering = System.getenv("PHYSICS_DEBUG_RENDERING")?.toBoolean() ?: false
    val showCursorWorldPosition = System.getenv("SHOW_CURSOR_WORLD_POSITION")?.toBoolean() ?: false
}

