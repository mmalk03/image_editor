package model.canvas.linedrawing

import model.canvas.Coordinate
import model.canvas.CoverageCoordinate

interface ThickLineStrategy {
    fun getCoordinates(source: Coordinate, dest: Coordinate, thickness: Double): List<CoverageCoordinate>
}

