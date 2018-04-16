package model.shape

import java.util.*

class SymmetricBresenhamLineStrategy : LineStrategy() {
    override fun getCoordinates(source: Coordinate, dest: Coordinate): List<Coordinate> {
        val dx = dest.x - source.x
        val dy = dest.y - source.y
        var d = 2 * dy - dx

        val dE = 2 * dy
        val dNE = 2 * (dy - dx)

        var xf = source.x
        var yf = source.y
        var xb = dest.x
        var yb = dest.y

        val coordinates = LinkedList<Coordinate>()

        coordinates.add(Coordinate(xf, yf))
        coordinates.add(Coordinate(xb, yb))

        while (xf < xb) {
            ++xf
            --xb
            if (d < 0)
                d += dE
            else {
                d += dNE
                ++yf
                --yb
            }
            coordinates.add(Coordinate(xf, yf))
            coordinates.add(Coordinate(xb, yb))
        }
        return coordinates
    }
}