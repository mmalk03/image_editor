package model.canvas.linedrawing

import model.canvas.Coordinate

interface LineStrategy{
    fun getCoordinates(source: Coordinate, dest: Coordinate) : List<Coordinate>
}