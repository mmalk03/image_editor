package model.canvas

import com.google.inject.Inject
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import tornadofx.*

class CanvasModel @Inject constructor(private val lineStrategy: LineStrategy,
                                      private val circleStrategy: CircleStrategy,
                                      private val thickLineStrategy: ThickLineStrategy,
                                      private val shapeDrawer: IShapeDrawer,
                                      private val coverageShapeDrawer: ICoverageShapeDrawer) {
    enum class Shape {
        SQUARE
    }

    val circleRadiuses = listOf(10, 20, 40, 80)
    val lineThicknesses = listOf(0.25, 0.50, 1.0, 2.0, 4.0, 8.0, 16.0, 32.0)
    val shapes = Shape.values().map { it.toString() }
    private var originalImage: Image? = null

    val imageProperty = SimpleObjectProperty<Image>()
    private var image by imageProperty

    val circleRadiusProperty = SimpleIntegerProperty(circleRadiuses.first())
    private var circleRadius by circleRadiusProperty

    val lineThicknessProperty = SimpleDoubleProperty(lineThicknesses.first())
    private var lineThickness by lineThicknessProperty

    val shapeProperty = SimpleObjectProperty<String>(Shape.SQUARE.toString())
    private var shape by shapeProperty

    private val lineSquareDecorator = LineSquareDecorator(lineStrategy)

    fun drawLine(source: Coordinate, dest: Coordinate) {
        if (image == null) return
        val coordinates = lineStrategy.getCoordinates(source, dest)
        image = shapeDrawer.draw(image, coordinates)
    }

    fun drawCircle(origin: Coordinate) {
        if (image == null) return
        val coordinates = circleStrategy.getCoordinates(origin, circleRadius)
        image = shapeDrawer.draw(image, coordinates)
    }

    fun drawThickLine(source: Coordinate, dest: Coordinate) {
        if (image == null) return
        val coordinates = thickLineStrategy.getCoordinates(source, dest, lineThickness)
        image = coverageShapeDrawer.draw(image, coordinates)
    }

    fun drawPen(source: Coordinate, dest: Coordinate) {
        if (image == null) return
        val coordinates = lineSquareDecorator.getCoordinates(source, dest)
        image = shapeDrawer.draw(image, coordinates)
    }

    fun onOpenImage(openedImage: Image) {
        image = openedImage
        originalImage = openedImage
    }

    fun onResetImage() {
        image = originalImage
    }

    fun onCloseImage() {
        image = null
        originalImage = null
    }
}