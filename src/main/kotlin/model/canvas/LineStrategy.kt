package model.canvas

interface LineStrategy{
    fun getCoordinates(source: Coordinate, dest: Coordinate) : List<Coordinate>
}