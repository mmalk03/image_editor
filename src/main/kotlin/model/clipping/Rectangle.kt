package model.clipping

import model.canvas.Coordinate

class Rectangle(val topLeftCoordinate: Coordinate, val bottomRightCoordinate: Coordinate) {
    val left: Int
        get() = topLeftCoordinate.x
    val top: Int
        get() = topLeftCoordinate.y
    val right: Int
        get() = bottomRightCoordinate.x
    val bottom: Int
        get() = bottomRightCoordinate.y

    val topRightCoordinate: Coordinate
        get() = Coordinate(right, top)
    val bottomLeftCoordinate: Coordinate
        get() = Coordinate(left, bottom)
}