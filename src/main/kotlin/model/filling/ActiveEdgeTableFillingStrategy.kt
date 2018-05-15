package model.filling

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import java.util.*

class ActiveEdgeTableFillingStrategy : FillingStrategy {
    private val random = Random()
    private var color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())
    private val colorRedInc = random.nextDouble() / 0.2
    private val colorGreenInc = random.nextDouble() / 0.2
    private val colorBlueInc = random.nextDouble() / 0.2

    override fun fillPolygon(polygon: Polygon, image: Image): Image {
        val outputImage = WritableImage(image.width.toInt(), image.height.toInt())
        val pixelWriter = outputImage.pixelWriter
        val pixelReader = image.pixelReader

        for (i in 0 until image.width.toInt()) {
            for (j in 0 until image.height.toInt()) {
                pixelWriter.setColor(i, j, pixelReader.getColor(i, j))
            }
        }

        val edgeTable = EdgeTable(polygon, image.height.toInt())
        val activeEdgeTable = ActiveEdgeTable()
        var y = edgeTable.getFirstScanLine()

        while (!activeEdgeTable.isEmpty() or !edgeTable.isEmpty()) {
            activeEdgeTable.add(edgeTable.poll(y))
            activeEdgeTable.sort()
            activeEdgeTable.getXPairs().forEach {
                for (i in it.first.toInt() until it.second.toInt() + 1) {
                    try {
                        pixelWriter.setColor(i, y, getRandomColor())
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }
            y += 1
            activeEdgeTable.removeEdgesEndingAt(y)
            activeEdgeTable.updateX()
        }

        return outputImage
    }

    private fun getRandomColor(): Color {
        color = Color.color(clamp(color.red + colorRedInc), clamp(color.green + colorGreenInc), clamp(color.blue + colorBlueInc))
        return color
        //return Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())
    }

    private fun clamp(d: Double): Double {
        return d % 1.0
    }
}