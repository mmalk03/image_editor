package model.filling

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import java.util.*

class ActiveEdgeTableFillingStrategy : FillingStrategy {
    private val random = Random()

    override fun fillPolygon(polygon: Polygon, image: Image): Image {
        val outputImage = WritableImage(image.width.toInt(), image.height.toInt())
        val pixelWriter = outputImage.pixelWriter
        val pixelReader = image.pixelReader

        for (i in 0 until image.width.toInt()) {
            for (j in 0 until image.height.toInt()) {
                pixelWriter.setColor(i, j, pixelReader.getColor(i, j))
            }
        }

        //TODO: implement

        pixelWriter.setColor(10, 10, getRandomColor())
        return outputImage
    }

    private fun getRandomColor(): Color {
        return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())
    }
}