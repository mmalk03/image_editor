package model.filter.quantization

import javafx.scene.image.Image
import javafx.scene.paint.Color
import model.filter.FilterStrategy

class MedianCutFilterStrategy(val image: Image, colorPaletteSize: Int) : FilterStrategy() {

    private val colorPalette = getColorPalette(image, colorPaletteSize)

    override fun getColor(image: Image, i: Int, j: Int): Color {
        val originalColor = image.pixelReader.getColor(i, j)
        var index = 0
        var minDistance = Double.MAX_VALUE
        for (x in 0 until colorPalette.size) {
            val currentDistance = getDistanceBetweenColors(originalColor, colorPalette[x])
            if (currentDistance < minDistance) {
                minDistance = currentDistance
                index = x
            }
        }
        return colorPalette[index]
    }

    private fun getColorPalette(image: Image, colorPaletteSize: Int): List<Color> {
        val colorMap = getColorMap(image)
        if (colorMap.size <= colorPaletteSize) {
            return colorMap.keys.toList()
        } else {
            val colorBoxes = ArrayList<ColorBox>()
            colorBoxes.add(ColorBox(colorMap, 0))

            for (i in 1 until colorPaletteSize) {
                try {
                    val nextColorBox = getBoxToSplit(colorBoxes)
                    val pairOfBoxes = getBoxSplit(nextColorBox)
                    colorBoxes.remove(nextColorBox)
                    colorBoxes.add(pairOfBoxes.first)
                    colorBoxes.add(pairOfBoxes.second)
                } catch (e: NoMoreBoxesToSplitException) {
                    break
                }
            }
            return colorBoxes.map { colorBox -> getAverageColor(colorBox) }
        }
    }

    private fun getColorMap(image: Image): HashMap<Color, ColorWrapper> {
        val result = HashMap<Color, ColorWrapper>()
        for (i in 0 until image.width.toInt()) {
            (0 until image.height.toInt())
                    .map { image.pixelReader.getColor(i, it) }
                    .forEach {
                        if (result.containsKey(it)) {
                            val temp = result[it]
                            temp?.increment()
                        } else {
                            val colorWrapper = ColorWrapper(it)
                            result.put(it, colorWrapper)
                        }
                    }
        }
        return result
    }

    private fun getBoxToSplit(colorBoxes: List<ColorBox>): ColorBox {
        val boxesToSplit = ArrayList<ColorBox>()
        (0 until colorBoxes.size)
                .map { colorBoxes[it] }
                .filterTo(boxesToSplit) { it.colors.size > 1 } //split only boxes which have more than 1 color

        if (boxesToSplit.size == 0) {
            throw NoMoreBoxesToSplitException()
        } else {
            var minColorBox = boxesToSplit[0]
            var minLevel = minColorBox.level
            for (i in 1 until boxesToSplit.size) {
                val temp = boxesToSplit[i]
                if (temp.level < minLevel) {
                    minLevel = temp.level
                    minColorBox = temp
                }
            }
            return minColorBox
        }
    }

    private fun getBoxSplit(colorBox: ColorBox): Pair<ColorBox, ColorBox> {
        val dimension = getDimensionWithMaxRange(colorBox)
        val median = median(colorBox.colors.values.map { it.getValue(dimension) }.sorted())

        val leftColors = HashMap<Color, ColorWrapper>()
        val rightColors = HashMap<Color, ColorWrapper>()

        for (colorWrapper in colorBox.colors.values) {
            if (colorWrapper.getValue(dimension) <= median) {
                leftColors.put(colorWrapper.color, colorWrapper)
            } else {
                rightColors.put(colorWrapper.color, colorWrapper)
            }
        }
        return Pair(
                ColorBox(leftColors, colorBox.level + 1),
                ColorBox(rightColors, colorBox.level + 1))
    }

    private fun median(list: List<Double>): Double {
        return if (list.size % 2 == 0) {
            list.let { (it[it.size / 2] + it[(it.size - 1) / 2]) / 2 }
        } else {
            list.let { it[(it.size - 1) / 2] }
        }
    }

    private fun getDimensionWithMaxRange(colorBox: ColorBox): ColorBox.Dimension {
        val ranges = doubleArrayOf(
                colorBox.redMax - colorBox.redMin,
                colorBox.greenMax - colorBox.greenMin,
                colorBox.blueMax - colorBox.blueMin)
        val maxRange = ranges.max()

        return when (maxRange) {
            ranges[0] -> ColorBox.Dimension.RED
            ranges[1] -> ColorBox.Dimension.GREEN
            else -> ColorBox.Dimension.BLUE
        }
    }

    private fun getAverageColor(colorBox: ColorBox): Color {
        return Color.color(
                colorBox.colors.keys.map { color -> color.red }.average(),
                colorBox.colors.keys.map { color -> color.green }.average(),
                colorBox.colors.keys.map { color -> color.blue }.average())
    }
}