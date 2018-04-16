package viewmodel

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import model.shape.CanvasModel
import model.shape.Coordinate
import module.MainModule
import service.ImageFileChooserService
import tornadofx.*

class CanvasViewModel : ViewModel() {
    enum class Shape {
        LINE, CIRCLE
    }

    private val injector = Guice.createInjector(MainModule())
    private val imageService = injector.getInstance<ImageFileChooserService>()
    private var originalImage: Image? = null

    private var firstClickMade = false
    private lateinit var sourceMouseCoordinate: Coordinate
    val shapesProperty = FXCollections.observableArrayList(Shape.values().map { it.toString() })
    val shapeProperty = SimpleStringProperty("LINE")

    private val canvasModel = CanvasModel()
    val circleRadiusProperty = bind { canvasModel.circleRadiusProperty }
    val imageProperty = bind { canvasModel.imageProperty }
    val circleRadiusesProperty = FXCollections.observableArrayList(canvasModel.circleRadiuses)

    fun onMouseClick(event: MouseEvent) {
        when (Shape.valueOf(shapeProperty.value)) {
            Shape.LINE -> handleLineClick(event)
            Shape.CIRCLE -> handleCircleClick(event)
        }
    }

    private fun handleLineClick(event: MouseEvent) {
        if (originalImage == null) return
        val coordinate = Coordinate(event.sceneX.toInt(), event.sceneY.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return

        if (firstClickMade) {
            canvasModel.drawLine(sourceMouseCoordinate, coordinate)
        } else {
            sourceMouseCoordinate = coordinate
        }
        firstClickMade = !firstClickMade
    }

    private fun handleCircleClick(event: MouseEvent) {
        canvasModel.drawCircle(Coordinate(event.x.toInt(), event.y.toInt()))
    }

    private fun isCoordinateInsideImageBounds(coordinate: Coordinate, image: Image): Boolean {
        return coordinate.x < 0 || coordinate.x >= image.width ||
                coordinate.y < 0 || coordinate.y >= image.height
    }

    fun onOpenImage() {
        val image = imageService.image
        if (image != null) {
            canvasModel.onOpenImage(image)
        }
    }

    fun onSaveImage() {
        TODO("implement")
    }

    fun onResetImage() {
        canvasModel.onResetImage()
    }

    fun onCloseImage() {
        canvasModel.onCloseImage()
    }
}