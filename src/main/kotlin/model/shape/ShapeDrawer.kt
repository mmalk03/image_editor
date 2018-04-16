package model.shape

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

class ShapeDrawer {
    private val color = Color.color(0.0, 0.0, 0.0)

    fun draw(image: Image, coordinates: List<Coordinate>): Image {
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

        for (coordinate in coordinates){
            pixelWriter.setColor(coordinate.x, coordinate.y, color)
        }

        return outputImage
    }
}
