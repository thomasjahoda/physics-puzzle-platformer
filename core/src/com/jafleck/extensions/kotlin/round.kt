package com.jafleck.extensions.kotlin

import kotlin.math.pow
import kotlin.math.round

fun Float.round(decimals: Int): Float {
    val multiplier = 10.0.pow(decimals.toDouble()).toFloat()
    return round(this * multiplier) / multiplier
}

fun Double.round(decimals: Int): Double {
    val multiplier = 10.0.pow(decimals.toDouble()).toFloat()
    return round(this * multiplier) / multiplier
}
