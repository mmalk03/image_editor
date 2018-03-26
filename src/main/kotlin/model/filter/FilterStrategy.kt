package model.filter

import javafx.scene.image.Image
import javafx.scene.paint.Color

abstract class FilterStrategy {
    abstract fun getColor(image: Image, i: Int, j: Int): Color

    protected fun getDistanceBetweenColors(color1: Color, color2: Color): Double {
        return (Math.abs(color1.red - color2.red)) +
                Math.abs(color1.green - color2.green) +
                Math.abs(color1.blue - color2.blue) / 3.0
    }
}