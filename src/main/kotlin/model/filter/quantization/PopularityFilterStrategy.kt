package model.filter.quantization

import javafx.scene.image.Image
import javafx.scene.paint.Color
import model.filter.FilterStrategy

class PopularityFilterStrategy(val image: Image, colorPaletteSize: Int) : FilterStrategy() {

    val colorPalette = getColorPalette(image, colorPaletteSize)

    override fun getColor(image: Image, i: Int, j: Int): Color {
        return colorPalette.minBy { getDistanceBetweenColors(image.pixelReader.getColor(i, j), it) }!!
    }

    private fun getColorPalette(image: Image, colorPaletteSize: Int): List<Color> {
        //TODO create list of frequency of separate color, then Color.color(index, index % 256, index % 256*256)
        val colorMap = HashMap<Color, Int>(256 * 256 * 256)
        for (r in 0 until 256) {
            for (g in 0 until 256) {
                for (b in 0 until 256) {
                    colorMap[Color.color(r.toDouble() / 256, g.toDouble() / 256, b.toDouble() / 256)] = 0
                }
            }
        }

        val pixelReader = image.pixelReader
        for (i in 0 until image.width.toInt()) {
            for (j in 0 until image.height.toInt()) {
                colorMap[pixelReader.getColor(i, j)]?.inc()
            }
        }

        val sortedColorMap: Map<Color, Int> = colorMap.toList().sortedBy { pair -> pair.second }.toMap()
        val colorsWithMaxFrequency = ArrayList<Color>(colorPaletteSize)
        (0 until colorPaletteSize).mapTo(colorsWithMaxFrequency) { sortedColorMap.keys.elementAt(sortedColorMap.size - 1 - it) }
        return colorsWithMaxFrequency
    }
}