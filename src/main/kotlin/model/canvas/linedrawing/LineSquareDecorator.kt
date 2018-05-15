package model.canvas.linedrawing

import model.canvas.Coordinate

class LineSquareDecorator(lineStrategy: LineStrategy) : LineDecorator(lineStrategy) {
    override fun getCoordinates(source: Coordinate, dest: Coordinate): List<Coordinate> {
        val originalCoordinates = lineStrategy.getCoordinates(source, dest)
        val coordinates = ArrayList<Coordinate>(originalCoordinates.size * 9)
        for (c in originalCoordinates) {
            for (i in -1 until 2) {
                for (j in -1 until 2) {
                    coordinates.add(Coordinate(c.x + i, c.y + j))
                }
            }
        }
        return coordinates
    }
}