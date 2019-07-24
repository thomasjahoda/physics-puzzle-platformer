package com.jafleck.game.entities.presets

import com.jafleck.game.entities.config.GenericEntityConfig

data class Preset<CustomEntityConfig>(
    val name: String = "default",
    val genericConfig: GenericEntityConfig = GenericEntityConfig(),
    val customEntityConfig: CustomEntityConfig
) {

    fun customEntityConfig(): CustomEntityConfig {
        return customEntityConfig!!
    }
}

fun genericPreset(name: String = "default",
                  genericConfig: GenericEntityConfig = GenericEntityConfig()): Preset<Any> {
    return Preset(name,
        genericConfig,
        customEntityConfig = false)
}

fun <SpecificEntityCustomization> List<Preset<SpecificEntityCustomization>>.asMap(): Map<String, Preset<SpecificEntityCustomization>> {
    return this.associateBy { it.name }
}

fun <SpecificEntityCustomization> Map<String, Preset<SpecificEntityCustomization>>.getPresetOrDefault(name: String?): Preset<SpecificEntityCustomization> {
    return if (name != null) {
        get(name) ?: error("Could not find preset with name '$name'")
    } else {
        get("default") ?: error("No default preset has been specified. Select a preset.")
    }
}
