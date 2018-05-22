package viewmodel

import com.google.inject.Inject
import javafx.beans.property.Property
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import model.canvas.Coordinate
import model.filling.FillingModel
import service.BlankImageService
import service.ImageService

abstract class IFillingViewModel : MyViewModel() {
    abstract val isPatternSelectedProperty: Property<Boolean>
    abstract val imageProperty: Property<Image>
    abstract fun onMouseClick(event: MouseEvent)
}

class FillingViewModel @Inject constructor(private val imageService: ImageService,
                                           private val fillingModel: FillingModel) : IFillingViewModel() {
    override val isPatternSelectedProperty = bind { fillingModel.isPatternSelectedProperty }
    private var originalImage: Image? = null

    override val imageProperty = bind { fillingModel.imageProperty }

    init {
        val blankImageService = BlankImageService()
        blankImageService.loadImage()
        originalImage = blankImageService.image
        fillingModel.onOpenImage(originalImage!!)
    }

    override fun onMouseClick(event: MouseEvent) {
        if (originalImage == null) return
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return
        fillingModel.addCoordinate(coordinate)
    }

    private fun isCoordinateInsideImageBounds(coordinate: Coordinate, image: Image): Boolean {
        return coordinate.x >= 0 && coordinate.x < image.width &&
                coordinate.y >= 0 && coordinate.y < image.height
    }

    override fun onOpenImage() {
        val image = imageService.image ?: return
        originalImage = image
        fillingModel.onOpenImage(image)
    }

    override fun onSaveImage() {
        TODO("implement")
    }

    override fun onResetImage() {
        fillingModel.onResetImage()
    }

    override fun onCloseImage() {
        fillingModel.onCloseImage()
    }
}
