package model.clipping

import model.canvas.Coordinate

class Rectangle(c1: Coordinate, c2: Coordinate) {

    val left: Int
    val top: Int
    val right: Int
    val bottom: Int

    val topLeftCoordinate: Coordinate
    val topRightCoordinate: Coordinate
    val bottomLeftCoordinate: Coordinate
    val bottomRightCoordinate: Coordinate

    init {
        if (c1.x < c2.x) {
            left = c1.x
            right = c2.x
        } else {
            left = c2.x
            right = c1.x
        }
        if (c1.y < c2.y) {
            bottom = c1.y
            top = c2.y
        } else {
            bottom = c2.y
            top = c1.y
        }
        topLeftCoordinate = Coordinate(left, top)
        topRightCoordinate = Coordinate(right, top)
        bottomLeftCoordinate = Coordinate(left, bottom)
        bottomRightCoordinate = Coordinate(right, bottom)
    }
}