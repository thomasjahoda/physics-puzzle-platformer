package com.jafleck.extensions.libgdx.scenes.scene2d

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.KSelectBox
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.defaultStyle

inline fun <T> selectBoxOfSimpleElements(
    elements: List<T>,
    elementDisplayFunction: (T) -> String,
    style: String = defaultStyle,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: KSelectBox<CustomToStringElement<T>>.() -> Unit = {}
): KSelectBox<CustomToStringElement<T>> {
    val selectBox = KSelectBox<CustomToStringElement<T>>(skin, style).apply {
        elements.forEach {
            items.add(CustomToStringElement(it, elementDisplayFunction(it)))
        }
    }
    selectBox.init()
    selectBox.refreshItems()
    return selectBox
}

fun <T> KSelectBox<CustomToStringElement<T>>.select(element: T) {
    selected = items.find { it.element == element } ?: error("Could not find element $element in items $items")
}

class CustomToStringElement<T>(val element: T, private val customToStringValue: String) {
    override fun toString(): String {
        return customToStringValue
    }
}
