package model.filter.dithering

import javafx.scene.image.Image
import javafx.scene.paint.Color

class OrderedDitheringFilterStrategy(grayLevel: Int, private val ditherMatrixDimension: Int) : DitheringFilterStrategy(grayLevel) {

    private val ditherMatrix: DitherMatrix = DitherMatrix.getBayerDitherMatrix(ditherMatrixDimension)
    private val ditherMatrixDimensionSquared = ditherMatrixDimension * ditherMatrixDimension
    private var grayValues: DoubleArray = DoubleArray(grayLevel)

    init {
        val grayStep = 1.0 / (grayLevel - 1)
        for (i in 0 until grayLevel - 1) {
            grayValues[i] = grayStep * i
        }
        grayValues[grayValues.size - 1] = 1.0
    }

    override fun getColor(image: Image, i: Int, j: Int): Color {
        val intensity = grayscaleFilterStrategy.getIntensity(image, i, j)
        var col = Math.floor((grayLevel - 1) * intensity).toInt()
        val re = (grayLevel - 1) * intensity - col

        val threshold = ditherMatrix.get(i % ditherMatrixDimension, j % ditherMatrixDimension)
        val scaledThreshold = threshold.toDouble() / (ditherMatrixDimensionSquared + 1)

        if(re >= scaledThreshold)
            col++
        return Color.color(grayValues[col], grayValues[col], grayValues[col])
    }
}