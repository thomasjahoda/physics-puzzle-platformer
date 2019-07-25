package com.jafleck.game.entities.physics

object CollisionEntityCategory {
    const val none: Short = 0
    /**
     * Use if the entity itself defines with whom it wants to collide with and it usually is not referenced
     * by other entities for collision filtering.
     */
    const val default: Short = 1
    const val environment: Short = 2
    const val player: Short = 4
}
