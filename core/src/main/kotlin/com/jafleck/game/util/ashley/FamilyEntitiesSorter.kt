package com.jafleck.game.util.ashley

import com.badlogic.ashley.core.*
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.utils.Array
import java.util.Comparator

/**
 * Use within Ashley entity systems.
 * Call addedToEngine and removedFromEngine!
 */
class FamilyEntitiesSorter(
    val family: Family,
    private val comparator: Comparator<Entity>
) : EntityListener {
    private val sortedEntities: Array<Entity> = Array(false, 16)
    private val sortedEntitiesImmutable: ImmutableArray<Entity> = ImmutableArray(sortedEntities)
    private var shouldSort: Boolean = false

    /**
     * Call this if the sorting criteria have changed. The actual sorting will be delayed until the entities are processed.
     */
    fun forceSort() {
        shouldSort = true
    }

    private fun sortIfNecessary() {
        if (shouldSort) {
            sortedEntities.sort(comparator)
            shouldSort = false
        }
    }

    fun addedToEngine(engine: Engine) {
        val newEntities = engine.getEntitiesFor(family)
        sortedEntities.clear()
        if (newEntities.size() > 0) {
            for (i in 0 until newEntities.size()) {
                sortedEntities.add(newEntities.get(i))
            }
            sortedEntities.sort(comparator)
        }
        shouldSort = false
        engine.addEntityListener(family, this)
    }

    fun removedFromEngine(engine: Engine) {
        engine.removeEntityListener(this)
        sortedEntities.clear()
        shouldSort = false
    }

    override fun entityAdded(entity: Entity) {
        sortedEntities.add(entity)
        shouldSort = true
    }

    override fun entityRemoved(entity: Entity) {
        sortedEntities.removeValue(entity, true)
        shouldSort = true
    }

    /**
     * @return set of entities processed by the system
     */
    val entities: ImmutableArray<Entity>
        get() {
            sortIfNecessary()
            return sortedEntitiesImmutable
        }
}
