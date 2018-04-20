package model.filter

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color

interface IImageFilter {
    fun filter(image: Image, strategy: FilterStrategy): Image
}

class ImageFilter : IImageFilter {
    override fun filter(image: Image, strategy: FilterStrategy): Image {
        val outputImage = WritableImage(image.width.toInt(), image.height.toInt())
        val pixelWriter = outputImage.pixelWriter
        //val totalPixels = image.width.toLong() * image.height.toLong()
        //var processedPixels = 0.toLong()

        for (i in 0 until image.width.toInt()) {
            for (j in 0 until image.height.toInt()) {
                pixelWriter.setColor(i, j, strategy.getColor(image, i, j))
            }
        }
        return outputImage
    }
}