package model.filter.grayscale

import javafx.scene.image.Image
import model.filter.FilterStrategy

abstract class GrayscaleFilterStrategy : FilterStrategy() {
    abstract fun getIntensity(image: Image, i: Int, j: Int): Double
}