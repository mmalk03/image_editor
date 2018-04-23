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
                println("Coverage: " + coordinate.coverage)
                pixelWriter.setColor(coordinate.x, coordinate.y, getColor(coordinate.coverage))
            } catch (e: IndexOutOfBoundsException) {
            }
        }
        return outputImage
    }

    private fun getColor(coverage: Double): Color {
        val intensity = when {
            coverage < 0 -> 1.0
            coverage > 1.0 -> 0.0
            else -> 1.0 - coverage
        }
        return Color.color(intensity, intensity, intensity)
    }
}