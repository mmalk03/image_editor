package model.filter.quantization

import javafx.scene.image.Image
import javafx.scene.paint.Color
import model.filter.FilterStrategy

class PopularityFilterStrategy(val image: Image, val colorPaletteSize: Int) : FilterStrategy() {

    companion object {
        const val RED_MULTIPLIER = 255 * 255
        const val GREEN_MULTIPLIER = 255
        const val BLUE_MULTIPLIER = 1
    }

    private lateinit var colorPalette: ArrayList<Color>

    init {
        initColorPalette()
    }

    override fun getColor(image: Image, i: Int, j: Int): Color {
        return colorPalette.minBy { getDistanceBetweenColors(image.pixelReader.getColor(i, j), it) }!!
    }

    private fun initColorPalette() {
        val colorList = IntArray(256 * 256 * 256)

        colorPalette = ArrayList(colorPaletteSize)
        val pixelReader = image.pixelReader
        for (i in 0 until image.width.toInt()) {
            for (j in 0 until image.height.toInt()) {
                colorList[pixelReader.getColor(i, j).let {
                    it.red * RED_MULTIPLIER +
                            it.green * GREEN_MULTIPLIER +
                            it.blue * BLUE_MULTIPLIER
                }.toInt()]++
            }
        }
        (0 until colorPaletteSize).forEach {
            val maxFrequencyColor = colorList.max()!!
            val maxFrequencyColorIndex = colorList.indexOf(colorList.max()!!)

            val red = (maxFrequencyColor / RED_MULTIPLIER)
            val green = (maxFrequencyColor - red * RED_MULTIPLIER) / GREEN_MULTIPLIER
            val blue = (maxFrequencyColor - red * RED_MULTIPLIER - green * GREEN_MULTIPLIER) / BLUE_MULTIPLIER
            colorPalette.add(Color.color(
                    (red / 255.0),
                    (green / 255.0),
                    (blue / 255.0)))
            colorList[maxFrequencyColorIndex] = 0
        }
    }
}