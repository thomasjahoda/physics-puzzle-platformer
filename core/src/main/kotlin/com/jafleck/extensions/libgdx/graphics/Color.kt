package com.jafleck.extensions.libgdx.graphics

import com.badlogic.gdx.graphics.Color


fun Color.mulExceptAlpha(scalar: Float): Color {
    this.r *= scalar
    this.g *= scalar
    this.b *= scalar
    return clamp()
}

/**
 * @param percentage percentage between 0 and 1
 */
fun Color.lerpBetween(startColor: Color, endColor: Color, percentage: Float) {
    val colorToModify = this
    require(percentage in 0f..1f) { "Percentage for transition was $percentage but must be between 0 and 1" }
    // validate color was copied when determining initial value
    require(startColor !== colorToModify)
    require(endColor !== colorToModify)
    colorToModify.set(startColor)
    colorToModify.lerp(endColor, percentage)
}
