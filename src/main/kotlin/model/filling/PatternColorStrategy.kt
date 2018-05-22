package model.filling

import javafx.scene.paint.Color
import model.canvas.Coordinate

class PatternColorStrategy(startPoint: Coordinate) : ColorStrategy {

    //private val imagePattern: Image

    init {
//        imagePattern =

    }

    override fun getColor(x: Int, y: Int): Color {
        return Color.color(0.toDouble(), 0.toDouble(), 0.toDouble())
        // patternY = ((y - startPoint.y) % patternImage.height.toInt() + patternImage.height.toInt()) % patternImage.height.toInt()
        // patternX = ((x - startPoint.x) % patternImage.width.toInt() + patternImage.width.toInt()) % patternImage.width.toInt()
        // pixelWriter.setColor(x, y, patternReader.getColor(patternX, patternY))
    }
}