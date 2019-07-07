package com.jafleck.game.config

object PhysicsConfiguration {
    val debugRendering = System.getenv("PHYSICS_DEBUG_RENDERING")?.toBoolean() ?: false
}

