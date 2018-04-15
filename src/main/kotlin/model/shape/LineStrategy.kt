package model.shape

abstract class LineStrategy{
    abstract fun getCoordinates(x1: Int, y1: Int, x2: Int, y2: Int) : List<Pair<Int, Int>>
}