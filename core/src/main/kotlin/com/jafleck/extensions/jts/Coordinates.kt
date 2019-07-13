package com.jafleck.extensions.jts

import org.locationtech.jts.geom.Coordinate

fun FloatArray.toJtsCoordinates(): Array<Coordinate> {
    if (isEmpty()) return Array(0) { Coordinate() }
    val polygonCoordinates = Array(this.size / 2 + 1) { Coordinate() }
    for (i in 0 until polygonCoordinates.size - 1) {
        polygonCoordinates[i].x = this[i * 2].toDouble()
        polygonCoordinates[i].y = this[i * 2 + 1].toDouble()
    }
    polygonCoordinates[polygonCoordinates.size - 1] = polygonCoordinates[0].copy()
    return polygonCoordinates
}

fun Array<Coordinate>.to2dVerticesArray(): FloatArray {
    val floatArray = FloatArray(this.size * 2)
    for (i in 0 until this.size) {
        floatArray[i * 2] = this[i].x.toFloat()
        floatArray[i * 2 + 1] = this[i].y.toFloat()
    }
    return floatArray
}
