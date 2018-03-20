package model.filter.grayscale

import javafx.scene.image.Image
import javafx.scene.paint.Color

class ScientificGrayscaleFilterStrategy : GrayscaleFilterStrategy() {
    override fun getIntensity(image: Image, i: Int, j: Int): Double {
        val pixel = image.pixelReader.getColor(i, j)
        return pixel.blue * 0.11 +
                pixel.red * 0.59 +
                pixel.green * 0.30
    }

    override fun getColor(image: Image, i: Int, j: Int): Color {
        val intensity = getIntensity(image, i, j)
        return Color.color(intensity, intensity, intensity)
    }
}