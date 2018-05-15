package model.clipping

import com.google.inject.Inject
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import model.canvas.Coordinate
import model.canvas.IShapeDrawer
import model.canvas.linedrawing.LineStrategy
import service.BigBlankImageService
import tornadofx.*

class ClippingModel @Inject constructor(private val lineStrategy: LineStrategy,
                                        private val clippingStrategy: ClippingStrategy,
                                        private val shapeDrawer: IShapeDrawer) {
    private var originalImage: Image? = null
    private val bigBlankImageService = BigBlankImageService()

    val imageProperty = SimpleObjectProperty<Image>()
    private var image by imageProperty

    init {
        bigBlankImageService.loadImage()
    }

    fun drawLine(source: Coordinate, dest: Coordinate, clipRect: Rectangle) {
        if (image == null) return
        val sourceDestCoordinates = clippingStrategy.getSourceDestCoordinates(source, dest, clipRect)
        if (sourceDestCoordinates != null) {
            val coordinates = lineStrategy.getCoordinates(sourceDestCoordinates.first, sourceDestCoordinates.second)
            image = shapeDrawer.draw(image, coordinates)
        }
    }

    fun drawRectangle(rect: Rectangle) {
        if (image == null) return
        val coordinatesTop = lineStrategy.getCoordinates(rect.topLeftCoordinate, rect.topRightCoordinate)
        image = shapeDrawer.draw(image, coordinatesTop)
        val coordinatesRight = lineStrategy.getCoordinates(rect.topRightCoordinate, rect.bottomRightCoordinate)
        image = shapeDrawer.draw(image, coordinatesRight)
        val coordinatesBottom = lineStrategy.getCoordinates(rect.bottomRightCoordinate, rect.bottomLeftCoordinate)
        image = shapeDrawer.draw(image, coordinatesBottom)
        val coordinatesLeft = lineStrategy.getCoordinates(rect.bottomLeftCoordinate, rect.topLeftCoordinate)
        image = shapeDrawer.draw(image, coordinatesLeft)
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