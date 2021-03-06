package viewmodel

import com.google.inject.Inject
import javafx.beans.property.Property
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import model.canvas.Coordinate
import model.clipping.ClippingModel
import model.clipping.Rectangle
import service.BlankImageService
import service.ImageService

abstract class IClippingViewModel : MyViewModel() {
    abstract val isRectangleSelectedProperty: SimpleBooleanProperty
    abstract val imageProperty: Property<Image>
    abstract fun onMouseClick(event: MouseEvent)
}

class ClippingViewModel @Inject constructor(private val imageService: ImageService,
                                            private val clippingModel: ClippingModel) : IClippingViewModel() {
    private var originalImage: Image? = null

    private var firstClickMade = false
    private var firstClickRectMade = false

    private lateinit var sourceMouseCoordinate: Coordinate
    private lateinit var sourceMouseRectCoordinate: Coordinate

    private var clipRect: Rectangle? = null

    override val imageProperty = bind { clippingModel.imageProperty }
    override val isRectangleSelectedProperty = SimpleBooleanProperty(true)

    init {
        val blankImageService = BlankImageService()
        blankImageService.loadImage()
        originalImage = blankImageService.image
        clippingModel.onOpenImage(originalImage!!)
    }

    override fun onMouseClick(event: MouseEvent) {
        if (isRectangleSelectedProperty.value) {
            handleRectangleClick(event)
        } else {
            handleLineClick(event)
        }
    }

    private fun handleLineClick(event: MouseEvent) {
        if (originalImage == null) return
        if (clipRect == null) return
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return

        if (firstClickMade) {
            clippingModel.drawLine(sourceMouseCoordinate, coordinate, clipRect!!)
        } else {
            sourceMouseCoordinate = coordinate
        }
        firstClickMade = !firstClickMade
    }

    private fun handleRectangleClick(event: MouseEvent) {
        if (originalImage == null) return
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return

        if (firstClickRectMade) {
            clipRect = Rectangle(sourceMouseRectCoordinate, coordinate)
            clippingModel.drawRectangle(clipRect!!)
        } else {
            sourceMouseRectCoordinate = coordinate
        }
        firstClickRectMade = !firstClickRectMade
    }

    private fun isCoordinateInsideImageBounds(coordinate: Coordinate, image: Image): Boolean {
        return coordinate.x >= 0 && coordinate.x < image.width &&
                coordinate.y >= 0 && coordinate.y < image.height
    }

    override fun onOpenImage() {
        val image = imageService.image ?: return
        originalImage = image
        clippingModel.onOpenImage(image)
    }

    override fun onSaveImage() {
        TODO("implement")
    }

    override fun onResetImage() {
        clippingModel.onResetImage()
    }

    override fun onCloseImage() {
        clippingModel.onCloseImage()
    }
}
