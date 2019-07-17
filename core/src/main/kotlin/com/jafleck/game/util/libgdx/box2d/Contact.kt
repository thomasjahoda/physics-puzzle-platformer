package com.jafleck.game.util.libgdx.box2d

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.Fixture
import com.jafleck.extensions.kotlin.Quadruple
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.util.ashley.getDebugDump

/**
 * Returns first fixture and it's entity that has the given component.
 * @return first (entity, fixture, otherFixture, isFixtureA) or null if neither fixture belongs to an entity having the component
 */
fun Contact.getEntityWithFixtureByComponentOrNull(componentClass: ComponentMapperAccessor<out Component>): Quadruple<Entity, Fixture, Fixture, Boolean>? {
    if (fixtureA.body.entity.getOrNull(componentClass) != null) {
        return Quadruple(fixtureA.body.entity, fixtureA, fixtureB, true)
    }
    if (fixtureB.body.entity.getOrNull(componentClass) != null) {
        return Quadruple(fixtureB.body.entity, fixtureB, fixtureA, false)
    }
    return null
}


fun Contact.debugDump() {
    println(getDebugDump())
}

private fun Contact.getDebugDump(): String {
    return "Contact: ${this}" +
        "============================================\n" +
        "A:\n" +
        "${fixtureA.body.entity.getDebugDump()}\n" +
        "============================\n" +
        "B:\n" +
        "${fixtureB.body.entity.getDebugDump()}\n" +
        "============================" +
        "============================================\n"
}
