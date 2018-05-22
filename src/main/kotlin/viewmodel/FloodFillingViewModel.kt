package viewmodel

import com.google.inject.Inject
import javafx.beans.property.Property
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import model.canvas.Coordinate
import model.flood.FloodFillingModel
import service.BlankImageService
import service.ImageService

abstract class IFloodFillingViewModel : MyViewModel() {
    abstract val imageProperty: Property<Image>
    abstract val thresholdProperty: Property<Number>
    abstract val thresholdsProperty: ObservableList<Int>
    abstract fun onMouseClick(event: MouseEvent)
}

class FloodFillingViewModel @Inject constructor(private val imageService: ImageService,
                                                private val floodFillingModel: FloodFillingModel) : IFloodFillingViewModel() {
    private var originalImage: Image? = null

    override val thresholdsProperty = FXCollections.observableArrayList(floodFillingModel.thresholds)!!
    override val thresholdProperty = bind { floodFillingModel.thresholdProperty }
    override val imageProperty = bind { floodFillingModel.imageProperty }

    init {
        val blankImageService = BlankImageService()
        blankImageService.loadImage()
        originalImage = blankImageService.image
        floodFillingModel.onOpenImage(originalImage!!)
    }

    override fun onMouseClick(event: MouseEvent) {
        if (originalImage == null) return
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return
        floodFillingModel.fill(coordinate)
    }

    private fun isCoordinateInsideImageBounds(coordinate: Coordinate, image: Image): Boolean {
        return coordinate.x >= 0 && coordinate.x < image.width &&
                coordinate.y >= 0 && coordinate.y < image.height
    }

    override fun onOpenImage() {
        val image = imageService.image ?: return
        originalImage = image
        floodFillingModel.onOpenImage(image)
    }

    override fun onSaveImage() {
        TODO("implement")
    }

    override fun onResetImage() {
        floodFillingModel.onResetImage()
    }

    override fun onCloseImage() {
        floodFillingModel.onCloseImage()
    }
}
