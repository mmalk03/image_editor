package model.canvas

import com.google.inject.Inject
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import model.canvas.linedrawing.*
import service.BigBlankImageService
import tornadofx.*

class CanvasModel @Inject constructor(private val lineStrategy: LineStrategy,
                                      private val circleStrategy: CircleStrategy,
                                      private val thickLineStrategy: ThickLineStrategy,
                                      private val superSamplingStrategy: SuperSamplingStrategy,
                                      private val shapeDrawer: IShapeDrawer,
                                      private val coverageShapeDrawer: ICoverageShapeDrawer) {
    enum class Shape {
        CIRCLE, SQUARE
    }

    val circleRadiuses = listOf(10, 20, 40, 80)
    val lineThicknesses = listOf(0.25, 0.50, 1.0, 2.0, 4.0, 8.0, 16.0, 32.0)
    val penThicknesses = listOf(1.0, 3.0, 5.0, 7.0)
    val shapes = Shape.values().map { it.toString() }
    private var originalImage: Image? = null
    private val bigBlankImageService = BigBlankImageService()

    val imageProperty = SimpleObjectProperty<Image>()
    private var image by imageProperty

    val circleRadiusProperty = SimpleIntegerProperty(circleRadiuses.first())
    private var circleRadius by circleRadiusProperty

    val lineThicknessProperty = SimpleDoubleProperty(lineThicknesses.first())
    private var lineThickness by lineThicknessProperty

    val penThicknessProperty = SimpleDoubleProperty(penThicknesses.first())
    private var penThickness by penThicknessProperty

    val shapeProperty = SimpleObjectProperty<String>(Shape.SQUARE.toString())
    private var shape by shapeProperty

    private val lineSquareDecorator = LineSquareDecorator(lineStrategy)

    init {
        bigBlankImageService.loadImage()
    }

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
        val coordinates = when (shape) {
            Shape.CIRCLE.toString() -> {
                val lineCircleDecorator = LineCircleDecorator(lineStrategy, penThickness.toInt())
                lineCircleDecorator.getCoordinates(source, dest)
            }
            Shape.SQUARE.toString() -> lineSquareDecorator.getCoordinates(source, dest)
            else -> null
        }
        if (coordinates != null) {
            image = shapeDrawer.draw(image, coordinates)
        }
    }

    fun drawSuperSampling(source: Coordinate, dest: Coordinate) {
        if (image == null) return
        val lineCircleDecorator = LineCircleDecorator(lineStrategy, penThickness.toInt())
        val coordinates = lineCircleDecorator.getCoordinates(
                Coordinate(source.x * 2, source.y * 2), Coordinate(dest.x * 2, dest.y * 2))
        val img = shapeDrawer.draw(bigBlankImageService.image!!, coordinates)
        val superSampledCoordinates = superSamplingStrategy.getCoordinates(img)
        image = coverageShapeDrawer.draw(image, superSampledCoordinates)
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