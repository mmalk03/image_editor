package model.filter.quantization

import javafx.scene.paint.Color

class ColorBox(val colors: HashMap<Color, ColorWrapper>, val level: Int) {

    enum class Dimension { RED, GREEN, BLUE }

    val redMin: Double
    val redMax: Double
    val greenMin: Double
    val greenMax: Double
    val blueMin: Double
    val blueMax: Double

    init {
        redMin = colors.keys.map { color -> color.red }.min() ?: 0.0
        redMax = colors.keys.map { color -> color.red }.max() ?: 0.0
        greenMin = colors.keys.map { color -> color.green }.min() ?: 0.0
        greenMax = colors.keys.map { color -> color.green }.max() ?: 0.0
        blueMin = colors.keys.map { color -> color.blue }.min() ?: 0.0
        blueMax = colors.keys.map { color -> color.blue }.max() ?: 0.0
    }
}