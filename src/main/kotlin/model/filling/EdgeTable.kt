package model.filling

import model.canvas.Coordinate
import java.util.*

class EdgeTable(polygon: Polygon, length: Int) {

    private val buckets: Array<LinkedList<Edge>> = Array(length, { LinkedList<Edge>() })

    init {
        var source: Coordinate
        var dest = polygon.first()
        var yMin: Int
        var yMax: Int
        var x: Int
        var slope: Double
        polygon.coordinates.drop(1).plus(dest).forEach {
            source = dest
            dest = it
            if (source.y < dest.y) {
                yMin = source.y
                yMax = dest.y
                x = source.x
            } else {
                yMin = dest.y
                yMax = source.y
                x = dest.x
            }
            slope = (source.x - dest.x).toDouble() / (source.y - dest.y).toDouble()
            buckets[yMin].add(Edge(yMax, x.toDouble(), slope))
            buckets[yMin].sortBy { edge -> edge.xMin }
        }
    }

    fun getFirstScanLine(): Int {
        return buckets.indexOfFirst { bucket -> bucket.size > 0 }
    }

    fun poll(i: Int): LinkedList<Edge> {
        val edges = LinkedList<Edge>()
        buckets[i].forEach { edges.add(it) }
        buckets[i].clear()
        return edges
    }

    fun isEmpty(): Boolean {
        return buckets.all { bucket -> bucket.size == 0 }
    }
}