package com.jafleck.game.gadgets

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

interface MouseActivatedGadget : Gadget {

    fun activate(handler: Entity, targetPosition: Vector2)
}
