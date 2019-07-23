package com.jafleck.extensions.ashley.utils

import com.badlogic.ashley.utils.ImmutableArray

fun <T> ImmutableArray<T>.isEmpty() = size() == 0
fun <T> ImmutableArray<T>.isNotEmpty() = size() != 0
