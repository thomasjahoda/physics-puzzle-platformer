package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.jafleck.extensions.ashley.utils.isEmpty
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.map.ActiveGameMapComponent
import ktx.ashley.allOf

inline class ActiveGameMapEntity(val entity: Entity) {

    val activeGameMap
        get() = entity[ActiveGameMapComponent]

    companion object {
        private val family: Family = allOf(
            ActiveGameMapComponent::class
        ).get()

        fun getCurrentMap(engine: Engine): ActiveGameMapEntity? {
            val entities = engine.getEntitiesFor(family)
            return if (entities.isEmpty()) {
                null
            } else {
                ActiveGameMapEntity(entities[0])
            }
        }

        fun hasActiveMap(engine: Engine) = getCurrentMap(engine) != null
        fun isBeingUnloadedOrHasNoActiveMap(engine: Engine) = getCurrentMap(engine) == null
    }
}
