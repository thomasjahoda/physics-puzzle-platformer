package com.jafleck.game.util.box2d

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.utils.SnapshotArray
import com.jafleck.extensions.libgdx.utils.safeForEach
import com.jafleck.game.util.logger

class ContactListenerMultiplexer : ContactListener {
    private val listeners = SnapshotArray<ContactListener>(4)

    private val logger = logger(this::class)

    fun addListener(listener: ContactListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ContactListener) {
        listeners.removeValue(listener, true)
    }

    override fun beginContact(contact: Contact?) {
        if (contact == null) {
            logger.debug {
                "contact in beginContact is null. This happens with bodies consisting of multiple fixtures in the water. " +
                    "I don't know why this happens yet. It does not seem to happen for endContact."
            }
            return
        }
        listeners.safeForEach {
            it.beginContact(contact)
        }
    }

    override fun endContact(contact: Contact) {
        listeners.safeForEach {
            it.endContact(contact)
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold?) {
        listeners.safeForEach {
            it.preSolve(contact, oldManifold)
        }
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse?) {
        listeners.safeForEach {
            it.postSolve(contact, impulse)
        }
    }

}
