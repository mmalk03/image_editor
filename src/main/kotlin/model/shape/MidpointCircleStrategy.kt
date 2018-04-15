package model.shape

import java.util.*

class MidpointCircleStrategy : CircleStrategy() {
    override fun getCoordinates(x: Int, y: Int, r: Int): List<Pair<Int, Int>> {
        var d = 1 - r
        var x1 = x
        var y1 = y

        val coordinates = LinkedList<Pair<Int, Int>>()
        coordinates.add(Pair(x1, y1))

        while (y1 > x1) {
            if (d < 0)
            //move to E
                d += 2 * x1 + 3
            else
            //move to SE
            {
                d += 2 * x1 - 2 * y1 + 5
                --y1
            }
            ++x1
            coordinates.add(Pair(x1, y1))
        }
        return coordinates
    }
}