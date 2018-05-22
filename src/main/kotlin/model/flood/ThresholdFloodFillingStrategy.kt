package model.flood

import javafx.scene.image.Image
import javafx.scene.image.PixelReader
import javafx.scene.image.PixelWriter
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import model.canvas.Coordinate
import java.util.*

class ThresholdFloodFillingStrategy(private val threshold: Int) : FloodFillingStrategy {

    private lateinit var pixelWriter: PixelWriter
    private lateinit var pixelReader: PixelReader
    private var imageWidth = 0
    private var imageHeight = 0
    private lateinit var newColor: Color
    private lateinit var oldColor: Color

    override fun fill(image: Image, startCoordinate: Coordinate): Image {
        val outputImage = WritableImage(image.width.toInt(), image.height.toInt())
        val inputPixelReader = image.pixelReader
        pixelWriter = outputImage.pixelWriter
        pixelReader = outputImage.pixelReader
        imageWidth = image.width.toInt()
        imageHeight = image.height.toInt()

        for (i in 0 until imageWidth) {
            for (j in 0 until imageHeight) {
                pixelWriter.setColor(i, j, inputPixelReader.getColor(i, j))
            }
        }
        newColor = Color.color(0.8, 0.1, 0.3)
        oldColor = pixelReader.getColor(startCoordinate.x, startCoordinate.y)

        val stack = Stack<Coordinate>()
        stack.push(Coordinate(startCoordinate.x, startCoordinate.y))
        while (!stack.isEmpty()) {
            val coordinate = stack.pop()
            if (isCoordinateInsideImageBounds(coordinate.x, coordinate.y)) {
                val color = pixelReader.getColor(coordinate.x, coordinate.y)
                if (!isAlreadyColored(color, newColor)) {
                    if (isInRange(color, oldColor)) {
                        pixelWriter.setColor(coordinate.x, coordinate.y, newColor)
                        stack.push(Coordinate(coordinate.x + 1, coordinate.y))
                        stack.push(Coordinate(coordinate.x - 1, coordinate.y))
                        stack.push(Coordinate(coordinate.x, coordinate.y + 1))
                        stack.push(Coordinate(coordinate.x, coordinate.y - 1))
                    }
                }
            }
        }
        return outputImage
    }

    private fun isAlreadyColored(color1: Color, color2: Color): Boolean {
        return (color1.red * 10).toInt() == (color2.red * 10).toInt() &&
                (color1.green * 10).toInt() == (color2.green * 10).toInt() &&
                (color1.blue * 10).toInt() == (color2.blue * 10).toInt()
    }

    private fun isInRange(color1: Color, color2: Color): Boolean {
        return (Math.abs(color1.red - color2.red) * 255 +
                Math.abs(color1.green - color2.green) * 255 +
                Math.abs(color1.blue - color2.blue) * 255) < threshold
    }

    private fun isCoordinateInsideImageBounds(x: Int, y: Int): Boolean {
        return x in 0..(imageWidth - 1) && y in 0..(imageHeight - 1)
    }
}