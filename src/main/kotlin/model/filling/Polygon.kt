package model.filling

import model.canvas.Coordinate
import java.util.*

class Polygon(val coordinates: LinkedList<Coordinate>){
    fun add(coordinate: Coordinate){
        coordinates.add(coordinate)
    }
    fun first(): Coordinate{
        return coordinates.first()
    }
}