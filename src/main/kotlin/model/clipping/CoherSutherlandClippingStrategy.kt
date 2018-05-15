package model.clipping

import model.canvas.Coordinate
import kotlin.experimental.and
import kotlin.experimental.or

class CoherSutherlandClippingStrategy : ClippingStrategy {
    val LEFT: Byte = 1
    val RIGHT: Byte = 2
    val BOTTOM: Byte = 4
    val TOP: Byte = 8

    override fun getSourceDestCoordinates(source: Coordinate, dest: Coordinate, clipRect: Rectangle): Pair<Coordinate, Coordinate>? {
        val zeroByte: Byte = 0.toByte()
        var accept = false
        var done = false
        var outcode1: Byte = computeOutcode(source, clipRect)
        var outcode2: Byte = computeOutcode(dest, clipRect)
        var coord1 = Coordinate(0, 0)
        var coord2 = Coordinate(0, 0)
        do {
            if ((outcode1 or outcode2) == zeroByte) { //trivially accepted
                accept = true
                done = true
            } else if ((outcode1 and outcode2) != zeroByte) { //trivially rejected
                accept = false
                done = true
            } else { //subdivide
                val outcodeOut = if (outcode1 != zeroByte) {
                    outcode1
                } else {
                    outcode2
                }
                val c: Coordinate? = when {
                    (outcodeOut and TOP) != zeroByte -> Coordinate(source.x + (dest.x - source.x) * (clipRect.top - source.y) / (dest.y - source.y), clipRect.top)
                    (outcodeOut and BOTTOM) != zeroByte -> Coordinate(source.x + (dest.x - source.x) * (clipRect.bottom - source.y) / (dest.y - source.y), clipRect.bottom)
                    (outcodeOut and RIGHT) != zeroByte -> Coordinate(0, 0)
                    (outcodeOut and LEFT) != zeroByte -> Coordinate(0, 0)
                    else -> null
                }
                if (outcodeOut == outcode1) {
                    coord1 = c!!
                    outcode1 = computeOutcode(source, clipRect)
                } else {
                    coord2 = c!!
                    outcode2 = computeOutcode(dest, clipRect)
                }
            }
        } while (!done)
        return if (accept) {
            Pair(coord1, coord2)
        } else {
            null
        }
    }

    private fun computeOutcode(c: Coordinate, clipRect: Rectangle): Byte {
        var outcode: Byte = 0

        if (c.x > clipRect.right) {
            outcode = outcode or RIGHT
        } else if (c.x < clipRect.left) {
            outcode = outcode or LEFT
        }

        if (c.y > clipRect.top) {
            outcode = outcode or TOP
        } else if (c.y < clipRect.bottom) {
            outcode = outcode or BOTTOM
        }

        return outcode
    }

}