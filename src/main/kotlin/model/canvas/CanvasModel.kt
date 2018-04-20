package model.canvas

import com.google.inject.Inject
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import tornadofx.*

class CanvasModel @Inject constructor(private val lineStrategy: LineStrategy,
                                      private val circleStrategy: CircleStrategy,
                                      private val thickLineStrategy: ThickLineStrategy,
                                      private val shapeDrawer: IShapeDrawer,
                                      private val coverageShapeDrawer: ICoverageShapeDrawer) {
    val circleRadiuses = listOf(10, 20, 40, 80)
    val lineThicknesses = listOf(1, 2, 4, 8, 16, 32)
    private var originalImage: Image? = null

    val imageProperty = SimpleObjectProperty<Image>()
    private var image by imageProperty

    val circleRadiusProperty = SimpleIntegerProperty(circleRadiuses.first())
    private var circleRadius by circleRadiusProperty

    val lineThicknessProperty = SimpleIntegerProperty(lineThicknesses.first())
    private var lineThickness by lineThicknessProperty

    fun drawLine(source: Coordinate, dest: Coordinate) {
        if (image == null) return
        val coordinates = lineStrategy.getCoordinates(source, dest)
        image = shapeDrawer.draw(image, coordinates)
    }

    fun drawThickLine(source: Coordinate, dest: Coordinate) {
        if (image == null) return
        val coordinates = thickLineStrategy.getCoordinates(source, dest, lineThickness.toDouble())
        image = coverageShapeDrawer.draw(image, coordinates)
    }

    fun drawCircle(origin: Coordinate) {
        if (image == null) return
        val coordinates = circleStrategy.getCoordinates(origin, circleRadius)
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