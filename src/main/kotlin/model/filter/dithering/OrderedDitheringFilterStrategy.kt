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
        val threshold = ditherMatrix.get(i % ditherMatrixDimension, j % ditherMatrixDimension)
        val scaledThreshold = threshold.toDouble() / (ditherMatrixDimensionSquared)

        val thresholds = DoubleArray(grayLevel - 1)
        val thresholdStepLeft = scaledThreshold / (grayLevel / 2)
        for (i in 0 until (grayLevel) / 2) {
            thresholds[i] = (i + 1) * thresholdStepLeft
        }
        val thresholdStepRight = (1.0 - scaledThreshold) / (grayLevel / 2 - 1)
        val centerIndex = thresholds.size / 2 + 1
        for (i in centerIndex until grayLevel - 1) {
            thresholds[i] = scaledThreshold + (i + 1 - centerIndex) * thresholdStepRight
        }

        (0 until thresholds.size)
                .filter { intensity <= thresholds[it] }
                .map { grayValues[it] }
                .forEach { return Color.color(it, it, it) }

        val newIntensity = grayValues.last()
        return Color.color(newIntensity, newIntensity, newIntensity)
    }
}