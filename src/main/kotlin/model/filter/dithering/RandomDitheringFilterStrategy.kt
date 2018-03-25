package model.filter.dithering

import javafx.scene.image.Image
import javafx.scene.paint.Color
import java.util.*

class RandomDitheringFilterStrategy(grayLevel: Int) : DitheringFilterStrategy(grayLevel) {

    private val random = Random()
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
        val thresholds = DoubleArray(grayLevel - 1)
        for (x in 0 until thresholds.size) {
            thresholds[x] = getRandomThreshold()
        }
        thresholds.sort()
        (0 until thresholds.size)
                .filter { intensity <= thresholds[it] }
                .map { grayValues[it] }
                .forEach { return Color.color(it, it, it) }
        val newIntensity = grayValues.last()
        return Color.color(newIntensity, newIntensity, newIntensity)
    }

    private fun getRandomThreshold(): Double {
        return random.nextDouble()
    }
}