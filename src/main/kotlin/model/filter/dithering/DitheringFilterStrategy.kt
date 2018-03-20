package model.filter.dithering

import model.filter.FilterStrategy
import model.filter.grayscale.GrayscaleFilterStrategy
import model.filter.grayscale.ScientificGrayscaleFilterStrategy

abstract class DitheringFilterStrategy(val grayLevel: Int) : FilterStrategy() {
    val grayscaleFilterStrategy: GrayscaleFilterStrategy = ScientificGrayscaleFilterStrategy()
}