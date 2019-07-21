package com.jafleck.extensions.libgdx.scenes.scene2d

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Value

fun <T : Actor> Cell<T>.percentWidthOfTable(percentFrom0To1: Float): Cell<T> {
    require(percentFrom0To1 <= 1f)
    return width(Value.percentWidth(percentFrom0To1, table))
}

fun <T : Actor> Cell<T>.percentHeightOfTable(percentFrom0To1: Float): Cell<T> {
    require(percentFrom0To1 <= 1f)
    return height(Value.percentHeight(percentFrom0To1, table))
}
