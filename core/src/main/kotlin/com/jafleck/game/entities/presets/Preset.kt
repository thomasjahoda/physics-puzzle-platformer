package com.jafleck.game.entities.presets

import com.jafleck.game.entities.customizations.GenericEntityCustomization

data class Preset(
    val name: String = "default",
    val genericCustomization: GenericEntityCustomization = GenericEntityCustomization()) {

    fun applyIfNotNull(other: Preset?): Preset {
        return if (other == null) {
            this
        } else {
            apply(other)
        }
    }

    fun apply(other: Preset): Preset {
        genericCustomization.apply(other.genericCustomization)
        return this
    }
}

fun List<Preset>.asMap(): Map<String, Preset> {
    return this.associateBy { it.name }
}

fun Map<String, Preset>.getPresetOrDefault(name: String?): Preset {
    return if (name != null) {
        get(name) ?: error("Could not find preset with name '$name'")
    } else {
        get("default") ?: error("No default preset has been specified. Select a preset.")
    }
}
