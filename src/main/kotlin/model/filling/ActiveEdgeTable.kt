package model.filling

import java.util.*

class ActiveEdgeTable {
    private val elements = LinkedList<Edge>()

    fun add(edges: LinkedList<Edge>) {
        elements.addAll(edges)
    }

    fun sort() {
        elements.sortBy { it.xMin }
    }

    fun getXPairs(): List<Pair<Double, Double>> {
        return elements.map { it.xMin }.chunked(2).map { Pair(it[0], it[1]) }
    }

    fun removeEdgesEndingAt(scanLine: Int) {
        elements.removeIf { it.yMax == scanLine }
    }

    fun updateX() {
        elements.forEach { it.xMin += it.slope }
    }

    fun isEmpty(): Boolean {
        return elements.isEmpty()
    }
}