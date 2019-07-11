package com.jafleck.game.util.libgdx.box2d

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Body


val Body.entity: Entity
    get() = userData as Entity
