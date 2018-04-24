package model.canvas

interface ThickLineStrategy {
    fun getCoordinates(source: Coordinate, dest: Coordinate, thickness: Double): List<CoverageCoordinate>
}

