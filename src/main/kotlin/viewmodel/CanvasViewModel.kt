package viewmodel

import com.google.inject.Inject
import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import model.canvas.CanvasModel
import model.canvas.Coordinate
import service.BlankImageService
import service.ImageService

abstract class ICanvasViewModel : MyViewModel() {
    abstract val shapesProperty: ObservableList<String>
    abstract val shapeProperty: SimpleStringProperty
    abstract val circleRadiusProperty: Property<Number>
    abstract val lineThicknessProperty: Property<Number>
    abstract val imageProperty: Property<Image>
    abstract val circleRadiusesProperty: ObservableList<Int>
    abstract val lineThicknessesProperty: ObservableList<Int>
    abstract fun onMouseClick(event: MouseEvent)
}

class CanvasViewModel @Inject constructor(private val imageService: ImageService,
                                          private val canvasModel: CanvasModel) : ICanvasViewModel() {
    enum class Shape {
        LINE, CIRCLE, THICK_LINE
    }

    private var originalImage: Image? = null

    private var firstClickMade = false
    private lateinit var sourceMouseCoordinate: Coordinate
    override val imageProperty = bind { canvasModel.imageProperty }
    override val shapeProperty = SimpleStringProperty(Shape.values().first().toString())
    override val circleRadiusProperty = bind { canvasModel.circleRadiusProperty }
    override val lineThicknessProperty = bind { canvasModel.lineThicknessProperty }
    override val shapesProperty = FXCollections.observableArrayList(Shape.values().map { it.toString() })!!
    override val circleRadiusesProperty = FXCollections.observableArrayList(canvasModel.circleRadiuses)!!
    override val lineThicknessesProperty = FXCollections.observableArrayList(canvasModel.lineThicknesses)!!

    init {
        val blankImageService = BlankImageService()
        blankImageService.loadImage()
        originalImage = blankImageService.image
        canvasModel.onOpenImage(originalImage!!)
    }

    override fun onMouseClick(event: MouseEvent) {
        when (Shape.valueOf(shapeProperty.value)) {
            Shape.LINE -> handleLineClick(event)
            Shape.CIRCLE -> handleCircleClick(event)
            Shape.THICK_LINE -> handleThickLineClick(event)
        }
    }

    private fun handleLineClick(event: MouseEvent) {
        if (originalImage == null) return
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return

        if (firstClickMade) {
            canvasModel.drawLine(sourceMouseCoordinate, coordinate)
        } else {
            sourceMouseCoordinate = coordinate
        }
        firstClickMade = !firstClickMade
    }

    private fun handleThickLineClick(event: MouseEvent) {
        if (originalImage == null) return
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return

        if (firstClickMade) {
            canvasModel.drawThickLine(sourceMouseCoordinate, coordinate)
        } else {
            sourceMouseCoordinate = coordinate
        }
        firstClickMade = !firstClickMade
    }

    private fun handleCircleClick(event: MouseEvent) {
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return
        canvasModel.drawCircle(coordinate)
    }

    private fun isCoordinateInsideImageBounds(coordinate: Coordinate, image: Image): Boolean {
        return coordinate.x >= 0 && coordinate.x < image.width &&
                coordinate.y >= 0 && coordinate.y < image.height
    }

    override fun onOpenImage() {
        val image = imageService.image ?: return
        originalImage = image
        canvasModel.onOpenImage(image)
    }

    override fun onSaveImage() {
        TODO("implement")
    }

    override fun onResetImage() {
        canvasModel.onResetImage()
    }

    override fun onCloseImage() {
        canvasModel.onCloseImage()
    }
}