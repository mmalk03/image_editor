package model.clipping

import model.canvas.Coordinate
import kotlin.experimental.and
import kotlin.experimental.or

class CoherSutherlandClippingStrategy : ClippingStrategy {
    val LEFT = 1.toByte()
    val RIGHT = 2.toByte()
    val BOTTOM = 4.toByte()
    val TOP = 8.toByte()

    override fun getSourceDestCoordinates(source: Coordinate, dest: Coordinate, clipRect: Rectangle): Pair<Coordinate, Coordinate>? {
        val zeroByte: Byte = 0.toByte()
        var accept = false
        var done = false
        var outcode1: Byte = computeOutcode(source, clipRect)
        var outcode2: Byte = computeOutcode(dest, clipRect)
        var c1 = Coordinate(source.x, source.y)
        var c2 = Coordinate(dest.x, dest.y)
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
                    (outcodeOut and TOP) != zeroByte -> Coordinate(c1.x + (c2.x - c1.x) * (clipRect.top - c1.y) / (c2.y - c1.y), clipRect.top)
                    (outcodeOut and BOTTOM) != zeroByte -> Coordinate(c1.x + (c2.x - c1.x) * (clipRect.bottom - c1.y) / (c2.y - c1.y), clipRect.bottom)
                    (outcodeOut and RIGHT) != zeroByte -> Coordinate(clipRect.right, c1.y + (c2.y - c1.y) * (clipRect.right - c1.x) / (c2.x - c1.x))
                    (outcodeOut and LEFT) != zeroByte -> Coordinate(clipRect.left, c1.y + (c2.y - c1.y) * (clipRect.left - c1.x) / (c2.x - c1.x))
                    else -> null
                }
                if (outcodeOut == outcode1) {
                    c1 = c!!
                    outcode1 = computeOutcode(c1, clipRect)
                } else {
                    c2 = c!!
                    outcode2 = computeOutcode(c2, clipRect)
                }
            }
        } while (!done)
        return if (accept) {
            Pair(c1, c2)
        } else {
            null
        }
    }

    private fun computeOutcode(c: Coordinate, clipRect: Rectangle): Byte {
        var outcode = 0.toByte()

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