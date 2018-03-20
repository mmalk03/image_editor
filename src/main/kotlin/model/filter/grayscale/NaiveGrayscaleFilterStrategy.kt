package model.filter.grayscale

import javafx.scene.image.Image
import javafx.scene.paint.Color

class NaiveGrayscaleFilterStrategy : GrayscaleFilterStrategy() {
    override fun getIntensity(image: Image, i: Int, j: Int): Double {
        val pixel = image.pixelReader.getColor(i, j)
        return pixel.blue / 3 +
                pixel.red / 3 +
                pixel.green / 3
    }

    override fun getColor(image: Image, i: Int, j: Int): Color {
        val intensity = getIntensity(image, i, j)
        return Color.color(intensity, intensity, intensity)
    }
}