package model.canvas

import java.util.*

class MidpointCircleStrategy : CircleStrategy() {
    override fun getCoordinates(origin: Coordinate, r: Int): List<Coordinate> {
        var d = 1 - r
        var x = origin.x
        var y = origin.y

        val coordinates = LinkedList<Coordinate>()
        coordinates.add(Coordinate(x, y))

        while (y > x) {
            if (d < 0)
            //move to E
                d += 2 * x + 3
            else
            //move to SE
            {
                d += 2 * x - 2 * y + 5
                --y
            }
            ++x
            coordinates.add(Coordinate(x, y))
        }
        return coordinates
    }
}