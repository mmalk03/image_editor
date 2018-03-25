package model.filter.quantization

import javafx.scene.image.Image
import javafx.scene.paint.Color
import model.filter.FilterStrategy

class MedianCutFilterStrategy(val image: Image, maxColors: Int) : FilterStrategy() {

    private val representativeColors = getRepresentativeColors(image, maxColors)

    override fun getColor(image: Image, i: Int, j: Int): Color {
        val originalColor = image.pixelReader.getColor(i, j)
        var index = 0
        var minDistance = Double.MAX_VALUE
        for (x in 0 until representativeColors.size) {
            val currentDistance = getDistanceBetweenColors(originalColor, representativeColors[x])
            if (currentDistance < minDistance) {
                minDistance = currentDistance
                index = x
            }
        }
        return representativeColors[index]
    }

    private fun getDistanceBetweenColors(color1: Color, color2: Color): Double {
        return (Math.abs(color1.red - color2.red)) +
                Math.abs(color1.green - color2.green) +
                Math.abs(color1.blue - color2.blue) / 3.0
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

    private fun getRepresentativeColors(image: Image, maxColors: Int): List<Color> {
        val colorMap = getColorMap(image)
        if (colorMap.size <= maxColors) {
            return colorMap.keys.toList()
        } else {
            val colorBoxes = ArrayList<ColorBox>()
            colorBoxes.add(ColorBox(colorMap, 0))

            for (i in 1 until maxColors) {
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

    private fun getBoxToSplit(colorBoxes: List<ColorBox>): ColorBox {
        val boxesToSplit = ArrayList<ColorBox>()
        (0 until colorBoxes.size)
                .map { colorBoxes[it] }
                .filterTo(boxesToSplit) { it.colors.size > 1 }

        if (boxesToSplit.size == 0) {
            throw NoMoreBoxesToSplitException()
        } else {
            var minColorBox = boxesToSplit[0]
            var minLevel = minColorBox.level
            for (i in 1 until boxesToSplit.size) {
                val test = boxesToSplit[i]
                if (minLevel > test.level) {
                    minLevel = test.level
                    minColorBox = test
                }
            }
            return minColorBox
        }
    }

    private fun getBoxSplit(colorBox: ColorBox): Pair<ColorBox, ColorBox> {
        val dimension = getMaxDimension(colorBox)
        val colorChannel = colorBox.colors.values.sumByDouble { it.getValue(dimension) }
        val median = colorChannel / colorBox.colors.size
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

    private fun getMaxDimension(colorBox: ColorBox): ColorBox.Dimension {
        val dimensions = doubleArrayOf(
                colorBox.redMax - colorBox.redMin,
                colorBox.greenMax - colorBox.greenMin,
                colorBox.blueMax - colorBox.blueMin)
        val maxSize = dimensions.max()

        return when (maxSize) {
            dimensions[0] -> ColorBox.Dimension.RED
            dimensions[1] -> ColorBox.Dimension.GREEN
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