package model.shape

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

class ShapeDrawer {
    private val color = Color.color(0.0, 0.0, 0.0)

    fun draw(image: Image, coordinates: List<Pair<Int, Int>>): Image {
        val outputImage = WritableImage(image.width.toInt(), image.height.toInt())
        val pixelWriter = outputImage.pixelWriter
        //val totalPixels = image.width.toLong() * image.height.toLong()
        //var processedPixels = 0.toLong()

        for (coordinate in coordinates){
            pixelWriter.setColor(coordinate.first, coordinate.second, color)
        }

        return outputImage
    }
}
