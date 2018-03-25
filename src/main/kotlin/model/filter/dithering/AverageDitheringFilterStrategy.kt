package model.filter.dithering

import javafx.scene.image.Image
import javafx.scene.paint.Color

class AverageDitheringFilterStrategy(grayLevel: Int) : DitheringFilterStrategy(grayLevel) {

    private var n: Int = 0
    private var mean: Double = 0.0
    private var meanDivisor: Double = grayLevel / 2.0
    private var thresholds: DoubleArray = DoubleArray(grayLevel - 1)
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
        updateMean(intensity)
        updateThresholds()
        (0 until thresholds.size)
                .filter { intensity <= thresholds[it] }
                .map { grayValues[it] }
                .first { return Color.color(it, it, it) }
        val newIntensity = grayValues.last()
        return Color.color(newIntensity, newIntensity, newIntensity)
    }

    private fun updateMean(intensity: Double) {
        n++
        mean = mean * (n - 1) / n + (intensity / n)
    }

    private fun updateThresholds() {
        val firstThreshold = mean / meanDivisor
        for (i in 0 until thresholds.size) {
            thresholds[i] = firstThreshold * (1 + i)
        }
    }
}