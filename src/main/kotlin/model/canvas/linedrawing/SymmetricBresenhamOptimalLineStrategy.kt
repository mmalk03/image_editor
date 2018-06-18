package model.canvas.linedrawing

import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color
import kotlin.math.abs

class SymmetricBresenhamOptimalLineStrategy : OptimalLineStrategy {

    override fun drawLine(srcX: Int, srcY: Int, destX: Int, destY: Int, imageWidth: Int, imageHeight: Int, pixelWriter: PixelWriter, color: Color) {
        var xSource = srcX
        var ySource = srcY
        var xDest = destX
        var yDest = destY

        //case 1
        val steep = abs(yDest - ySource) > abs(xDest - xSource)
        if (steep) {
            val tempSource = xSource
            xSource = ySource
            ySource = tempSource
            val tempDest = xDest
            xDest = yDest
            yDest = tempDest
        }

        //case 2
        if (xSource > xDest) {
            val xTemp = xSource
            xSource = xDest
            xDest = xTemp
            val yTemp = ySource
            ySource = yDest
            yDest = yTemp
        }

        //case 3
        val deltaY = when (ySource > yDest) {
            true -> -1
            false -> 1
        }

        val dx = xDest - xSource
        val dy = abs(yDest - ySource)
        var d = 2 * dy - dx
        val dE = 2 * dy
        val dNE = 2 * (dy - dx)

        if (steep) {
            drawPixel(ySource, xSource, imageWidth, imageHeight, pixelWriter, color)
            drawPixel(yDest, xDest, imageWidth, imageHeight, pixelWriter, color)
        } else {
            drawPixel(xSource, ySource, imageWidth, imageHeight, pixelWriter, color)
            drawPixel(xDest, yDest, imageWidth, imageHeight, pixelWriter, color)
        }

        while (xSource < xDest) {
            xSource += 1
            xDest -= 1
            if (d < 0) {
                d += dE
            } else {
                d += dNE
                ySource += deltaY
                yDest -= deltaY
            }
            if (steep) {
                drawPixel(ySource, xSource, imageWidth, imageHeight, pixelWriter, color)
                drawPixel(yDest, xDest, imageWidth, imageHeight, pixelWriter, color)
            } else {
                drawPixel(xSource, ySource, imageWidth, imageHeight, pixelWriter, color)
                drawPixel(xDest, yDest, imageWidth, imageHeight, pixelWriter, color)
            }

        }
    }

    private fun drawPixel(x: Int, y: Int, imageWidth: Int, imageHeight: Int, pixelWriter: PixelWriter, color: Color) {
        if (x > 0 && x < imageWidth - 1 && y > 0 && y < imageHeight - 1) {
            pixelWriter.setColor(x, y, color)
        }
    }
}