package model.filling

import javafx.scene.paint.Color

interface ColorStrategy {
    fun getColor(x: Int, y: Int): Color
}