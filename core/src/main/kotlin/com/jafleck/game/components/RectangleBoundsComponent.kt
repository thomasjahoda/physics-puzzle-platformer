package com.jafleck.game.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor

/**
 * Describes the size of a rectangle around the unrotated shape of the entity.
 * The entity can be shaped in any way, this component should merely be used to determine how big it is approximately.
 * This component should only be added to entities with a fixed or semi-fixed rectangular size.
 * It is not necessary on all entities, it should only be added if it is necessary in combination with some system/component that needs this info.
 */
class RectangleBoundsComponent(
    val vector: Vector2
) : Component {
    constructor(width: Float, height: Float) : this(Vector2(width, height))

    var width
        get() = vector.x
        set(value) {
            vector.x = value
        }
    var height
        get() = vector.y
        set(value) {
            vector.y = value
        }

    companion object : ComponentMapperAccessor<RectangleBoundsComponent>(RectangleBoundsComponent::class)
}
