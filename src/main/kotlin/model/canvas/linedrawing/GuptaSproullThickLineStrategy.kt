package model.canvas.linedrawing

import model.canvas.Coordinate
import model.canvas.CoverageCoordinate
import java.util.*
import kotlin.math.*

class GuptaSproullThickLineStrategy : ThickLineStrategy {
    private val coordinates = LinkedList<CoverageCoordinate>()

    override fun getCoordinates(source: Coordinate, dest: Coordinate, thickness: Double): List<CoverageCoordinate> {
        coordinates.clear()

        var xSource = source.x
        var xDest = dest.x
        var ySource = source.y
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
        var twoDx: Int
        val invDenominator = 1.0 / (2.0 * sqrt((dx * dx + dy * dy).toDouble()))
        val twoDxInvDenominator = 2.0 * dx * invDenominator
        var x = xSource
        var y = ySource

        var i: Int
//if deltaY <0 change to - +
        if (steep) {
            intensifyPixel(y, x, thickness, 0.0)
            i = 1
            while (intensifyPixel(y + i * deltaY, x, thickness, i * twoDxInvDenominator) > 0) i++
            i = 1
            while (intensifyPixel(y - i * deltaY, x, thickness, i * twoDxInvDenominator) > 0) i++
        } else {
            intensifyPixel(x, y, thickness, 0.0)
            i = 1
            while (intensifyPixel(x, y + i * deltaY, thickness, i * twoDxInvDenominator) > 0) i++
            i = 1
            while (intensifyPixel(x, y - i * deltaY, thickness, i * twoDxInvDenominator) > 0) i++
        }

        while (x < xDest) {
            if (d < 0) {
                twoDx = d + dx
                d += dE
            } else {
                twoDx = d - dx
                d += dNE
                y += deltaY
            }
            x += 1
            if (steep) {
                intensifyPixel(y, x, thickness, twoDx * invDenominator)
                i = 1
                while (intensifyPixel(y + i * deltaY, x, thickness, i * twoDxInvDenominator - twoDx * invDenominator) > 0) i++
                i = 1
                while (intensifyPixel(y - i * deltaY, x, thickness, i * twoDxInvDenominator + twoDx * invDenominator) > 0) i++
            } else {
                intensifyPixel(x, y, thickness, twoDx * invDenominator)
                i = 1
                while (intensifyPixel(x, y + i * deltaY, thickness, i * twoDxInvDenominator - twoDx * invDenominator) > 0) i++
                i = 1
                while (intensifyPixel(x, y - i * deltaY, thickness, i * twoDxInvDenominator + twoDx * invDenominator) > 0) i++
            }
        }
        return coordinates
    }

    private fun intensifyPixel(x: Int, y: Int, thickness: Double, distance: Double): Double {
        val coverage = getCoverage(thickness, distance)
        if (coverage > 0) {
            coordinates.add(CoverageCoordinate(x, y, coverage))
        }
        return coverage
    }

    private fun getCoverage(thickness: Double, distance: Double): Double {
        val w = thickness / 2.0
        val r = 0.5
        return when {
            w >= r -> when {
                w <= distance -> getCov(distance - w, r)
                else -> 1 - getCov(w - distance, r)
            }
            else -> when {
                distance <= w -> 1 - getCov(w - distance, r) - getCov(w + distance, r)
                distance <= r - w -> getCov(distance - w, r) - getCov(distance + w, r)
                else -> getCov(distance - w, r)
            }
        }
    }

    private fun getCov(d: Double, r: Double): Double {
        return if (d <= r) {
            (1 / PI) * acos(d / r) - (d / (PI * r * r)) * sqrt(r * r - d * d)
        } else {
            0.0
        }
    }
}