package com.jafleck.game.util.libgdx.box2d

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.Fixture
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.util.ashley.getDebugDump


inline fun <ComponentType : Component> Contact.processIfComponentInvolved(
    componentClass: ComponentMapperAccessor<ComponentType>,
    block: CollisionInvolvingComponentHandleFunction<ComponentType>) {
    val componentA = fixtureA.body.entity.getOrNull(componentClass)
    if (componentA != null) {
        block(fixtureA.body.entity, componentA, fixtureA, fixtureB)
    } else {
        val componentB = fixtureB.body.entity.getOrNull(componentClass)
        if (componentB != null) {
            block(fixtureB.body.entity, componentB, fixtureB, fixtureA)
        }
    }
}

typealias CollisionInvolvingComponentHandleFunction<ComponentType> = (ownerEntity: Entity, relevantComponent: ComponentType, ownerFixture: Fixture, otherFixture: Fixture) -> Unit

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
