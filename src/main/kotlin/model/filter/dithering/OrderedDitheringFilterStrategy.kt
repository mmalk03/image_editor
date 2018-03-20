package model.filter.dithering

import javafx.scene.image.Image
import javafx.scene.paint.Color

class OrderedDitheringFilterStrategy(grayLevel: Int) : DitheringFilterStrategy(grayLevel) {
    override fun getColor(image: Image, i: Int, j: Int): Color {
        TODO("implement")
        //val grayscaleColor = grayscaleFilterStrategy.getColor(image, i, j)
        //return getGrayValueForPixel(grayscaleColor)
    }

    private fun getGrayValueForPixel(grayscaleColor: Color): Color {
        return grayscaleColor
    }
}