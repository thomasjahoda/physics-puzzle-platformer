package com.jafleck.game.maploading

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.Array
import com.jafleck.game.util.logger


class MapUnloader(
    private val engine: Engine,
    private val world: World
) {
    private val logger = logger(this::class)

    fun unloadMap() {
        logger.debug { "Unloading current map" }
        removeAllEntities()
        removeOrphanedWorldBodies()
    }

    private fun removeAllEntities() {
        engine.removeAllEntities()
    }

    private fun removeOrphanedWorldBodies() {
        val bodies = Array<Body>()
        world.getBodies(bodies)
        if (!bodies.isEmpty) {
            logger.debug { "There are still ${bodies.size} orphaned world bodies which will be destroyed" }
            bodies.forEach { world.destroyBody(it) }
        }
    }
}
