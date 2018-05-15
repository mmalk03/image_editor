package model.canvas.linedrawing

import model.canvas.Coordinate
import java.util.*
import kotlin.math.abs

class SymmetricBresenhamLineStrategy : LineStrategy {
    override fun getCoordinates(source: Coordinate, dest: Coordinate): List<Coordinate> {
        var xSource = source.x
        var ySource = source.y
        var xDest = dest.x
        var yDest = dest.y

        //case 1
        val steep = abs(yDest - ySource) > abs(xDest - xSource)
        if (steep) {
            val tempSource = xSource
            xSource = ySource
            ySource = tempSource
            val tempDest = xDest
            xDest = yDest
            yDest = tempDest
        }

        //case 2
        if (xSource > xDest) {
            val xTemp = xSource
            xSource = xDest
            xDest = xTemp
            val yTemp = ySource
            ySource = yDest
            yDest = yTemp
        }

        //case 3
        val deltaY = when (ySource > yDest) {
            true -> -1
            false -> 1
        }

        val dx = xDest - xSource
        val dy = abs(yDest - ySource)
        var d = 2 * dy - dx
        val dE = 2 * dy
        val dNE = 2 * (dy - dx)

        val coordinates = LinkedList<Coordinate>()
        if (steep) {
            coordinates.add(Coordinate(ySource, xSource))
            coordinates.add(Coordinate(yDest, xDest))
        } else {
            coordinates.add(Coordinate(xSource, ySource))
            coordinates.add(Coordinate(xDest, yDest))
        }

        while (xSource < xDest) {
            xSource += 1
            xDest -= 1
            if (d < 0) {
                d += dE
            } else {
                d += dNE
                ySource += deltaY
                yDest -= deltaY
            }
            if (steep) {
                coordinates.add(Coordinate(ySource, xSource))
                coordinates.add(Coordinate(yDest, xDest))
            } else {
                coordinates.add(Coordinate(xSource, ySource))
                coordinates.add(Coordinate(xDest, yDest))
            }

        }
        return coordinates
    }
}