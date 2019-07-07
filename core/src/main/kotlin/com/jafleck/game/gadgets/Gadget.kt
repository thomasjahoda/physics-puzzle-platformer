package com.jafleck.game.gadgets

import com.badlogic.ashley.core.Entity

interface Gadget {

    fun selected(handler: Entity) {}

    fun onUnselect(handler: Entity) {}
}
