package model.canvas

import kotlin.math.PI

class LineCircleDecorator(lineStrategy: LineStrategy) : LineDecorator(lineStrategy) {

    private val r = 3

    override fun getCoordinates(source: Coordinate, dest: Coordinate): List<Coordinate> {
        val originalCoordinates = lineStrategy.getCoordinates(source, dest)
        val rSquared = r * r
        val coordinates = ArrayList<Coordinate>((originalCoordinates.size * PI * rSquared).toInt())
        for (c in originalCoordinates) {
            for (i in -r until r + 1) {
                for (j in -r until r + 1) {
                    if(i * i + j * j <= rSquared){
                        coordinates.add(Coordinate(c.x + i, c.y + j))
                    }
                }
            }
        }
        return coordinates
    }
}