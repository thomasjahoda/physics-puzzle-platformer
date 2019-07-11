package com.jafleck.extensions.libgdx.map

import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.XmlReader.Element

/**
 * Solves issue that color properties without value have an empty string attribute value and result in an exception in the default loader.
 * TODO create Github issue for LibGDX (or issue for Tiled that those properties should not be exported???)
 */
class PatchedTmxMapLoader(resolver: FileHandleResolver) : TmxMapLoader(resolver) {

    override fun loadProperties(properties: MapProperties, element: Element?) {
        if (element == null) return
        removeColorPropertiesWithoutValue(element)
        super.loadProperties(properties, element)
    }

    private fun removeColorPropertiesWithoutValue(element: Element) {
        if (element.name == "properties") {
            for (property in element.getChildrenByName("property")) {
                var value: String? = property.getAttribute("value", null)
                val type = property.getAttribute("type", null)
                if (value == null) {
                    value = property.text
                }
                if (value == "" && type == "color") {
                    element.removeChild(property)
                }
            }
        }
    }

}
