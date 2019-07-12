package com.jafleck.game.components.logic

import com.badlogic.ashley.core.Component
import com.jafleck.extensions.libgdxktx.ashley.ComponentMapperAccessor
import com.jafleck.game.entities.WaterEntity

class PushedUpByWaterComponent(val waterEntity: WaterEntity, val originalLinearDamping: Float) : Component {

    companion object : ComponentMapperAccessor<PushedUpByWaterComponent>(PushedUpByWaterComponent::class)
}
