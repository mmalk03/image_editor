package model.canvas

import javafx.scene.image.Image
import javafx.scene.paint.Color
import java.util.*

interface ISuperSamplingStrategy {
    fun getCoordinates(image: Image): List<CoverageCoordinate>
}

class SuperSamplingStrategy : ISuperSamplingStrategy {
    override fun getCoordinates(image: Image): List<CoverageCoordinate> {
        val coordinates = LinkedList<CoverageCoordinate>()
        val pixelReader = image.pixelReader
        for (i in 0 until image.height.toInt() step 2) {
            for (j in 0 until image.width.toInt() step 2) {
                val numberOfBlackPixels = isBlack(pixelReader.getColor(i, j)) +
                        isBlack(pixelReader.getColor(i, j + 1)) +
                        isBlack(pixelReader.getColor(i + 1, j)) +
                        isBlack(pixelReader.getColor(i + 1, j + 1))
                if (numberOfBlackPixels > 0) {
                    coordinates.add(CoverageCoordinate(i / 2, j / 2, numberOfBlackPixels / 4.0))
                }
            }
        }
        return coordinates
    }

    private fun isBlack(color: Color): Int {
        return if (color.blue == 0.0 && color.red == 0.0 && color.green == 0.0) {
            1
        } else {
            0
        }
    }
}