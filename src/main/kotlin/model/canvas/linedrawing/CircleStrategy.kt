package model.canvas.linedrawing

import model.canvas.Coordinate

abstract class CircleStrategy {
    abstract fun getCoordinates(origin: Coordinate, r: Int) : List<Coordinate>
}