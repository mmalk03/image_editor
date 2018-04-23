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
    abstract val drawingTypesProperty: ObservableList<String>
    abstract val drawingTypeProperty: SimpleStringProperty
    abstract val circleRadiusProperty: Property<Number>
    abstract val lineThicknessProperty: Property<Number>
    abstract val shapeProperty: Property<String>
    abstract val imageProperty: Property<Image>
    abstract val circleRadiusesProperty: ObservableList<Int>
    abstract val lineThicknessesProperty: ObservableList<Double>
    abstract val shapesProperty: ObservableList<String>
    abstract fun onMouseClick(event: MouseEvent)
}

class CanvasViewModel @Inject constructor(private val imageService: ImageService,
                                          private val canvasModel: CanvasModel) : ICanvasViewModel() {
    enum class DrawingType {
        LINE, CIRCLE, THICK_LINE, PEN
    }

    private var originalImage: Image? = null

    private var firstClickMade = false
    private lateinit var sourceMouseCoordinate: Coordinate
    override val imageProperty = bind { canvasModel.imageProperty }
    override val drawingTypeProperty = SimpleStringProperty(DrawingType.values().first().toString())
    override val circleRadiusProperty = bind { canvasModel.circleRadiusProperty }
    override val lineThicknessProperty = bind { canvasModel.lineThicknessProperty }
    override val shapeProperty = bind { canvasModel.shapeProperty }
    override val drawingTypesProperty = FXCollections.observableArrayList(DrawingType.values().map { it.toString() })!!
    override val circleRadiusesProperty = FXCollections.observableArrayList(canvasModel.circleRadiuses)!!
    override val lineThicknessesProperty = FXCollections.observableArrayList(canvasModel.lineThicknesses)!!
    override val shapesProperty = FXCollections.observableArrayList(canvasModel.shapes)!!

    init {
        val blankImageService = BlankImageService()
        blankImageService.loadImage()
        originalImage = blankImageService.image
        canvasModel.onOpenImage(originalImage!!)
    }

    override fun onMouseClick(event: MouseEvent) {
        when (DrawingType.valueOf(drawingTypeProperty.value)) {
            DrawingType.LINE -> handleLineClick(event)
            DrawingType.CIRCLE -> handleCircleClick(event)
            DrawingType.THICK_LINE -> handleThickLineClick(event)
            DrawingType.PEN -> handlePenClick(event)
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

    private fun handleCircleClick(event: MouseEvent) {
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return
        canvasModel.drawCircle(coordinate)
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

    private fun handlePenClick(event: MouseEvent) {
        if (originalImage == null) return
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return

        if (firstClickMade) {
            canvasModel.drawPen(sourceMouseCoordinate, coordinate)
        } else {
            sourceMouseCoordinate = coordinate
        }
        firstClickMade = !firstClickMade
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