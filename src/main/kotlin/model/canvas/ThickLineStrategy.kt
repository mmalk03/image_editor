package model.canvas

abstract class ThickLineStrategy {
    abstract fun getCoordinates(source: Coordinate, dest: Coordinate, thickness: Double): List<CoverageCoordinate>
}

