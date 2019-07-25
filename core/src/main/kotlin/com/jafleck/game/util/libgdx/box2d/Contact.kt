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
    block: CollisionInvolvingComponentHandleFunction<ComponentType>
) {
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

inline fun <ComponentTypeA : Component, ComponentTypeB : Component> Contact.processIfComponentsCollide(
    componentClassA: ComponentMapperAccessor<ComponentTypeA>,
    componentClassB: ComponentMapperAccessor<ComponentTypeB>,
    block: CollisionInvolvingTwoComponentsHandleFunction<ComponentTypeA, ComponentTypeB>
) {
    val entityA = fixtureA.body.entity
    val entityB = fixtureB.body.entity
    val componentAFromEntityA = entityA.getOrNull(componentClassA)
    if (componentAFromEntityA != null) {
        val componentB = entityB.getOrNull(componentClassB)
        if (componentB != null) {
            block(entityA, componentAFromEntityA, fixtureA, entityB, componentB, fixtureB)
        }
    } else {
        val componenAFromEntityB = entityB.getOrNull(componentClassA)
        if (componenAFromEntityB != null) {
            val componentB = entityA.getOrNull(componentClassB)
            if (componentB != null) {
                block(entityB, componenAFromEntityB, fixtureB, entityA, componentB, fixtureA)
            }
        }
    }
}

typealias CollisionInvolvingTwoComponentsHandleFunction<ComponentTypeA, ComponentTypeB> = (
    entityA: Entity, componentA: ComponentTypeA, fixtureA: Fixture,
    entityB: Entity, componentB: ComponentTypeB, fixtureB: Fixture
) -> Unit

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
