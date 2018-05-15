package model.clipping

import model.canvas.Coordinate

interface ClippingStrategy {
    fun getSourceDestCoordinates(source: Coordinate, dest: Coordinate, clipRect: Rectangle): Pair<Coordinate, Coordinate>?
}