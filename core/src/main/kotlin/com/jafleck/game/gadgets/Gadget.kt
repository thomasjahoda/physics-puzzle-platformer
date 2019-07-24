package com.jafleck.game.gadgets

import com.badlogic.ashley.core.Entity

interface Gadget {

    val name: String

    fun selected(handler: Entity) {}

    fun unselected(handler: Entity) {}
}
