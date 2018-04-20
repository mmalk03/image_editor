package model.canvas

abstract class LineStrategy{
    abstract fun getCoordinates(source: Coordinate, dest: Coordinate) : List<Coordinate>
}