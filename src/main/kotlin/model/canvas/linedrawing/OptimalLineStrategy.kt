package model.canvas.linedrawing

import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color

interface OptimalLineStrategy {
    fun drawLine(srcX: Int, srcY: Int, destX: Int, destY: Int, imageWidth: Int, imageHeight: Int, pixelWriter: PixelWriter, color: Color)
}