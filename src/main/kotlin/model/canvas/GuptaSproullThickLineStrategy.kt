package model.canvas

import java.util.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sqrt

class GuptaSproullThickLineStrategy : ThickLineStrategy() {
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

        if (steep) {
            intensifyPixel(y, x, thickness, 0.0)
            intensifyPixel(y, x + 1, thickness, 0.0)
            intensifyPixel(y, x - 1, thickness, 0.0)
        } else {
            intensifyPixel(x, y, thickness, 0.0)
            intensifyPixel(x, y + 1, thickness, 0.0)
            intensifyPixel(x, y - 1, thickness, 0.0)
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
                intensifyPixel(y, x + 1, thickness, twoDxInvDenominator - twoDx * invDenominator)
                intensifyPixel(y, x - 1, thickness, twoDxInvDenominator + twoDx * invDenominator)
            } else {
                intensifyPixel(x, y, thickness, twoDx * invDenominator)
                intensifyPixel(x, y + 1, thickness, twoDxInvDenominator - twoDx * invDenominator)
                intensifyPixel(x, y - 1, thickness, twoDxInvDenominator + twoDx * invDenominator)
            }
        }
        return coordinates
    }

    private fun intensifyPixel(x: Int, y: Int, thickness: Double, distance: Double) {
        val coverage = getCoverage(thickness, distance)
        if (coverage > 0) {
            coordinates.add(CoverageCoordinate(x, y, coverage))
        }
    }

    private fun getCoverage(thickness: Double, distance: Double): Double {
        val w = thickness / 2.0
        //TODO find out what does this r mean
        val r = 2.0
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
            0.5 - (d * sqrt(r * r - d * d)) / (PI * (r * r)) -
                    1 / (PI * Math.asin(d / r))
        } else {
            0.0
        }
    }
}