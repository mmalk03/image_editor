package model.canvas

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

interface ICoverageShapeDrawer {
    fun draw(image: Image, coordinates: List<CoverageCoordinate>): Image
}

class CoverageShapeDrawer : ICoverageShapeDrawer {
    override fun draw(image: Image, coordinates: List<CoverageCoordinate>): Image {
        val outputImage = WritableImage(image.width.toInt(), image.height.toInt())
        val pixelWriter = outputImage.pixelWriter
        val pixelReader = image.pixelReader
        //val totalPixels = image.width.toLong() * image.height.toLong()
        //var processedPixels = 0.toLong()

        for (i in 0 until image.width.toInt()) {
            for (j in 0 until image.height.toInt()) {
                pixelWriter.setColor(i, j, pixelReader.getColor(i, j))
            }
        }

        for (coordinate in coordinates) {
            try {
                pixelWriter.setColor(coordinate.x, coordinate.y,
                        Color.color(clamp(1.0 - coordinate.coverage), clamp(1.0 - coordinate.coverage), clamp(1.0 - coordinate.coverage)))
            } catch (e: IndexOutOfBoundsException) {
            }
        }
        return outputImage
    }

    private fun clamp(value: Double): Double {
        return when {
            value < 0 -> 0.0
            value > 1 -> 1.0
            else -> value
        }
    }
}