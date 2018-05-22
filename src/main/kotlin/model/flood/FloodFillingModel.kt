package model.flood

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import model.canvas.Coordinate
import service.BigBlankImageService
import tornadofx.*

class FloodFillingModel {
    val thresholds = listOf(10, 20, 40, 80)

    private var originalImage: Image? = null
    private val bigBlankImageService = BigBlankImageService()

    val imageProperty = SimpleObjectProperty<Image>()
    private var image by imageProperty

    val thresholdProperty = SimpleIntegerProperty(thresholds.first())
    private var threshold by thresholdProperty

    init {
        bigBlankImageService.loadImage()
    }

    fun fill(coordinate: Coordinate) {
        val floodFillingStrategy = ThresholdFloodFillingStrategy(threshold)
        image = floodFillingStrategy.fill(image, coordinate)
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