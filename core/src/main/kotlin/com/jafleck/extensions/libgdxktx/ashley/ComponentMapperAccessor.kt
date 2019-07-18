package com.jafleck.extensions.libgdxktx.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import kotlin.reflect.KClass

open class ComponentMapperAccessor<T : Component>(val componentClass: KClass<T>) {
    private var componentMapper = ComponentMapper.getFor(componentClass.java)

    fun getFrom(entity: Entity): T = componentMapper[entity]
        ?: error("Entity does not have component of type ${componentClass.simpleName}")

    fun getFromOrNull(entity: Entity): T? = componentMapper[entity]

    fun isIn(entity: Entity): Boolean = componentMapper.has(entity)
}

operator fun <T : Component> Entity.contains(componentMapperAccessor: ComponentMapperAccessor<T>): Boolean {
    return componentMapperAccessor.isIn(this)
}

fun <T : Component> Entity.has(componentMapperAccessor: ComponentMapperAccessor<T>): Boolean {
    return componentMapperAccessor.isIn(this)
}

operator fun <T : Component> Entity.get(componentMapperAccessor: ComponentMapperAccessor<T>): T {
    return componentMapperAccessor.getFrom(this)
}

fun <T : Component> Entity.getOrNull(componentMapperAccessor: ComponentMapperAccessor<T>): T? {
    return componentMapperAccessor.getFromOrNull(this)
}
