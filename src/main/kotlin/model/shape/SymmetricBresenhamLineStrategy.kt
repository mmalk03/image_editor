package model.shape

import java.util.*

class SymmetricBresenhamLineStrategy : LineStrategy() {
    override fun getCoordinates(x1: Int, y1: Int, x2: Int, y2: Int): List<Pair<Int, Int>> {
        val dx = x2 - x1
        val dy = y2 - y1
        var d = 2 * dy - dx

        val dE = 2 * dy
        val dNE = 2 * (dy - dx)

        var xf = x1
        var yf = y1
        var xb = x2
        var yb = y2

        val coordinates = LinkedList<Pair<Int, Int>>()

        coordinates.add(Pair(xf, yf))
        coordinates.add(Pair(xb, yb))

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
            coordinates.add(Pair(xf, yf))
            coordinates.add(Pair(xb, yb))
        }
        return coordinates
    }
}