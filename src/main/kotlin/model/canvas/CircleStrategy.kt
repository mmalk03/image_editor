package model.canvas

abstract class CircleStrategy {
    abstract fun getCoordinates(origin: Coordinate, r: Int) : List<Coordinate>
}