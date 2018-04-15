package model.shape

abstract class CircleStrategy {
    abstract fun getCoordinates(x: Int, y: Int, r: Int) : List<Pair<Int, Int>>
}