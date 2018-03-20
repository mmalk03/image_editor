package model.filter

import javafx.scene.image.Image
import javafx.scene.paint.Color

abstract class FilterStrategy {
    abstract fun getColor(image: Image, i: Int, j: Int): Color
}