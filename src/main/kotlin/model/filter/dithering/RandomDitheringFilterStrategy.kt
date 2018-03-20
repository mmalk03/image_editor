package model.filter.dithering

import javafx.scene.image.Image
import javafx.scene.paint.Color
import java.util.*

class RandomDitheringFilterStrategy(grayLevel: Int) : DitheringFilterStrategy(grayLevel) {

    private val random = Random()
    private var grayValues: DoubleArray

    init {
        grayValues = DoubleArray(grayLevel)
        val grayStep = 1.0 / (grayLevel - 1)
        grayValues[0] = 0.0
        for (i in 1 until grayLevel - 1) {
            grayValues[i] = grayStep * i
        }
        grayValues[grayValues.size - 1] = 1.0
    }

    override fun getColor(image: Image, i: Int, j: Int): Color {
        val intensity = grayscaleFilterStrategy.getIntensity(image, i, j)
        return mapToGrayColor(intensity)
    }

    private fun mapToGrayColor(intensity: Double): Color {
        val thresholds = DoubleArray(grayLevel - 1)
        for (i in 0 until thresholds.size) {
            thresholds[i] = getRandomThreshold()
        }
        thresholds.sort()

        for (i in 0 until thresholds.size) {
            if (intensity <= thresholds[i]) {
                val newIntensity = grayValues[i]
                return Color.color(newIntensity, newIntensity, newIntensity)
            }
        }
        val newIntensity = grayValues[grayValues.size - 1]
        return Color.color(newIntensity, newIntensity, newIntensity)
    }

    private fun getRandomThreshold(): Double {
        return random.nextDouble()
    }
}