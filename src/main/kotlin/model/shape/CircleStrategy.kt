package model.shape

abstract class CircleStrategy {
    abstract fun getCoordinates(origin: Coordinate, r: Int) : List<Coordinate>
}