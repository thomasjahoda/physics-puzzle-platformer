package com.jafleck.extensions.libgdxktx.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import kotlin.reflect.KClass

open class ComponentMapperAccessor<T : Component>(componentClass: KClass<T>) {
    private var componentMapper = ComponentMapper.getFor(componentClass.java)

    operator fun get(entity: Entity): T = componentMapper[entity]

    fun isIn(entity: Entity): Boolean = componentMapper.has(entity)
}

operator fun <T : Component> Entity.contains(componentMapperAccessor: ComponentMapperAccessor<T>): Boolean {
    return componentMapperAccessor.isIn(this)
}
