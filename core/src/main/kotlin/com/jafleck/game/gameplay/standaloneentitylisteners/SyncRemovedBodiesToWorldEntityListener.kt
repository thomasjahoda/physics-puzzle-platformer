package com.jafleck.game.gameplay.standaloneentitylisteners

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.BodyComponent
import com.jafleck.game.util.ashley.EntityFamilyListener
import com.jafleck.game.util.logger
import ktx.ashley.allOf


class SyncRemovedBodiesToWorldEntityListener(
    private val world: World
) : EntityFamilyListener {

    private val logger = logger(this::class)

    override val family: Family
        get() = allOf(
            BodyComponent::class
        ).get()

    override fun entityAdded(entity: Entity) {
    }

    override fun entityRemoved(entity: Entity) {
        logger.debug { "Destroying body and all attached joints of entity $entity" }
        world.destroyBody(entity[BodyComponent].value)
    }
}
