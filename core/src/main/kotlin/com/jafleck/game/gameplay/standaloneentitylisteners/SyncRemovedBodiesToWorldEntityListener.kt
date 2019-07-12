package com.jafleck.game.gameplay.standaloneentitylisteners

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.basic.BodyComponent
import com.jafleck.game.util.listeners.EntityFamilyListener
import ktx.ashley.allOf


class SyncRemovedBodiesToWorldEntityListener(
    private val world: World
) : EntityFamilyListener {

    override val family: Family
        get() = allOf(
            BodyComponent::class
        ).get()

    override fun entityAdded(entity: Entity) {
    }

    override fun entityRemoved(entity: Entity) {
        world.destroyBody(entity[BodyComponent].value)
    }
}
