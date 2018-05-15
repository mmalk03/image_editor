package model.filling

import java.util.*

class ActiveEdgeTable {
    private val elements = LinkedList<Edge>()

    fun add(edges: LinkedList<Edge>) {
        elements.addAll(edges)
    }

    fun sort() {
        elements.sortBy { edge -> edge.xMin }
    }

    fun getXPairs(): List<Pair<Double, Double>> {
        return elements.map { edge -> edge.xMin }.chunked(2).map { list -> Pair(list[0], list[1]) }
    }

    fun removeEdgesEndingAt(scanLine: Int) {
        elements.removeIf { edge -> edge.yMax == scanLine }
    }

    fun updateX() {
        elements.forEach { edge -> edge.xMin += edge.slope }
    }

    fun isEmpty(): Boolean {
        return elements.isEmpty()
    }
}