package model.shape

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import module.MainModule
import tornadofx.*

class CanvasModel {
    val circleRadiuses = listOf(10, 20, 40, 80)

    private val injector = Guice.createInjector(MainModule())
    private var originalImage: Image? = null

    val imageProperty = SimpleObjectProperty<Image>()
    private var image by imageProperty

    val circleRadiusProperty = SimpleIntegerProperty(10)
    private var circleRadius by circleRadiusProperty

    private val lineStrategy = injector.getInstance<LineStrategy>()
    private val circleStrategy = injector.getInstance<CircleStrategy>()
    private val shapeDrawer = ShapeDrawer()

    fun drawLine(source: Coordinate, dest: Coordinate) {
        val coordinates = lineStrategy.getCoordinates(source, dest)
        image = shapeDrawer.draw(image, coordinates)
    }

    fun drawCircle(origin: Coordinate) {
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