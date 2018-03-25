package model.filter.dithering

import javafx.scene.image.Image
import javafx.scene.paint.Color

class OrderedDitheringFilterStrategy(grayLevel: Int, private val ditherMatrixDimension: Int) : DitheringFilterStrategy(grayLevel) {

    private val ditherMatrix: DitherMatrix = DitherMatrix.getBayerDitherMatrix(ditherMatrixDimension)
    private val ditherMatrixDimensionSquared = ditherMatrixDimension * ditherMatrixDimension

    override fun getColor(image: Image, i: Int, j: Int): Color {
        val intensity = grayscaleFilterStrategy.getIntensity(image, i, j)
        val threshold = ditherMatrix.get(i % ditherMatrixDimension, j % ditherMatrixDimension)
        val scaledThreshold = threshold.toDouble() / (ditherMatrixDimensionSquared)

        return if (intensity < scaledThreshold) {
            Color.BLACK
        } else {
            Color.WHITE
        }
    }
}