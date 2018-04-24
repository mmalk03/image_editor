package service

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

class BigBlankImageService : ImageService() {
    private val width = 840
    private val height = 840
    private val white = Color.color(1.0, 1.0, 1.0)
    override var image: Image? = null

    override fun loadImage() {
        val outputImage = WritableImage(width, height)
        val pixelWriter = outputImage.pixelWriter

        for (i in 0 until outputImage.width.toInt()) {
            for (j in 0 until outputImage.height.toInt()) {
                pixelWriter.setColor(i, j, white)
            }
        }
        image = outputImage
    }
}