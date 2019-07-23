package com.jafleck.game.entities

import com.badlogic.ashley.core.Entity
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.map.ActiveGameMapComponent

inline class ActiveGameMapEntity(val entity: Entity) {

    val activeGameMap
        get() = entity[ActiveGameMapComponent]

}
