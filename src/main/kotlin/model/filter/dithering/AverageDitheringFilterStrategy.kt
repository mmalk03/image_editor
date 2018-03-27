package model.filter.dithering

import javafx.scene.image.Image
import javafx.scene.paint.Color

class AverageDitheringFilterStrategy(image: Image, grayLevel: Int) : DitheringFilterStrategy(grayLevel) {

    private var grayValues: DoubleArray = DoubleArray(grayLevel)
    private var means: DoubleArray = DoubleArray(grayLevel - 1)
    private var colorsInGivenRange = ArrayList<ArrayList<Double>>()

    init {
        (0 until grayLevel - 1).forEach { colorsInGivenRange.add(ArrayList()) }

        val grayStep = 1.0 / (grayLevel - 1)
        for (i in 0 until grayLevel - 1) {
            grayValues[i] = grayStep * i
        }
        grayValues[grayValues.size - 1] = 1.0

        for (i in 0 until image.width.toInt()) {
            (0 until image.height.toInt())
                    .map { grayscaleFilterStrategy.getIntensity(image, i, it) }
                    .forEach { intensity ->
                        (1 until grayValues.size)
                                .filter { intensity < grayValues[it] }
                                .forEach { colorsInGivenRange[it - 1].add(intensity) }
                    }
        }

        (0 until means.size).forEach {
            means[it] = colorsInGivenRange[it].sum() / colorsInGivenRange[it].size
        }
    }

    override fun getColor(image: Image, i: Int, j: Int): Color {
        val intensity = grayscaleFilterStrategy.getIntensity(image, i, j)
        (1 until grayValues.size)
                .filter { intensity < grayValues[it] }
                .forEach {
                    return if (intensity < means[it - 1]) {
                        Color.color(grayValues[it - 1], grayValues[it - 1], grayValues[it - 1])
                    } else {
                        Color.color(grayValues[it], grayValues[it], grayValues[it])
                    }
                }
        throw RuntimeException()
    }
}