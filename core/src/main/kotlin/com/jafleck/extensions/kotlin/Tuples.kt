package com.jafleck.extensions.kotlin

import java.io.Serializable

/**
 * @see Triple
 */
public data class Quadruple<out A, out B, out C, out D>(
    public val first: A,
    public val second: B,
    public val third: C,
    public val fourth: D
) : Serializable {

    /**
     * Returns string representation of the [Triple] including its [first], [second], [third] and [fourth] values.
     */
    public override fun toString(): String = "($first, $second, $third, $fourth)"
}

public fun <T> Quadruple<T, T, T, T>.toList(): List<T> = listOf(first, second, third, fourth)
