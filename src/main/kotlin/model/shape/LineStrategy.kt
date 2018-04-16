package model.shape

abstract class LineStrategy{
    abstract fun getCoordinates(source: Coordinate, dest: Coordinate) : List<Coordinate>
}