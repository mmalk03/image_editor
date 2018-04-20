package model.canvas

import java.util.*

class MidpointCircleStrategy : CircleStrategy() {
    override fun getCoordinates(origin: Coordinate, r: Int): List<Coordinate> {
        val xOrigin = origin.x
        val yOrigin = origin.y

        var d = 1 - r
        var x = 0
        var y = r
        val coordinates = LinkedList<Coordinate>()
        coordinates.add(Coordinate(xOrigin, yOrigin + r))
        coordinates.add(Coordinate(xOrigin, yOrigin - r))
        coordinates.add(Coordinate(xOrigin + r, yOrigin))
        coordinates.add(Coordinate(xOrigin - r, yOrigin))

        while (y > x) {
            if (d < 0) {
                d += 2 * x + 3
            } else {
                d += 2 * x - 2 * y + 5
                y -= 1
            }
            x += 1
            coordinates.add(Coordinate(xOrigin + x, yOrigin + y))
            coordinates.add(Coordinate(xOrigin + x, yOrigin - y))
            coordinates.add(Coordinate(xOrigin - x, yOrigin + y))
            coordinates.add(Coordinate(xOrigin - x, yOrigin - y))
            coordinates.add(Coordinate(xOrigin + y, yOrigin + x))
            coordinates.add(Coordinate(xOrigin + y, yOrigin - x))
            coordinates.add(Coordinate(xOrigin - y, yOrigin + x))
            coordinates.add(Coordinate(xOrigin - y, yOrigin - x))
        }
        return coordinates
    }
}