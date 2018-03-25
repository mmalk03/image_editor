package model.filter.quantization

import javafx.scene.paint.Color

class ColorWrapper(val color: Color) {

    private var count: Int = 1

    fun getValue(dimension: ColorBox.Dimension): Double {
        return when (dimension) {
            ColorBox.Dimension.RED -> color.red
            ColorBox.Dimension.GREEN -> color.green
            ColorBox.Dimension.BLUE -> color.blue
        }
    }

    fun increment() {
        count++
    }
}