package model.filling

import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class ActiveEdgeTableFillingStrategy : FillingStrategy {

    override fun fillPolygon(polygon: Polygon, image: Image, colorStrategy: ColorStrategy): Image {
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
                for (x in it.first.toInt() until it.second.toInt() + 1) {
                    pixelWriter.setColor(x, y, colorStrategy.getColor(x, y))
                }
            }
            y += 1
            activeEdgeTable.removeEdgesEndingAt(y)
            activeEdgeTable.updateX()
        }

        return outputImage
    }
}