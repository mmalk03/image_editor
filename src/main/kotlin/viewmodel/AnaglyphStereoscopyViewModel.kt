package viewmodel

import com.google.inject.Inject
import javafx.beans.property.Property
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import model.anaglyphstereoscopy.AnaglyphStereoscopyModel
import model.canvas.Coordinate
import service.BlankImageService
import service.ImageService

abstract class IAnaglyphStereoscopyViewModel : MyViewModel() {
    abstract val imageProperty: Property<Image>
    abstract val shapeProperty: Property<String>
    abstract val shapesProperty: ObservableList<String>
    abstract val cRadiusProperty: Property<Number>
    abstract val cRadiusesProperty: ObservableList<Int>
    abstract val cHeightProperty: Property<Number>
    abstract val cHeightsProperty: ObservableList<Int>
    abstract val sphereRadiusProperty: Property<Number>
    abstract val sphereRadiusesProperty: ObservableList<Int>
    abstract val cuboidEdgeLengthProperty: Property<Number>
    abstract val cuboidEdgeLengthsProperty: ObservableList<Int>
    abstract val meshDensityProperty: Property<Number>
    abstract val meshDensitiesProperty: ObservableList<Int>

    abstract fun onMouseClick(event: MouseEvent)
}

class AnaglyphStereoscopyViewModel @Inject constructor(private val imageService: ImageService,
                                                       private val anaglyphStereoscopyModel: AnaglyphStereoscopyModel) : IAnaglyphStereoscopyViewModel() {

    override val shapeProperty = bind { anaglyphStereoscopyModel.shapeProperty }
    override val shapesProperty = FXCollections.observableArrayList(anaglyphStereoscopyModel.shapes)!!
    override val cRadiusProperty = bind { anaglyphStereoscopyModel.cRadiusProperty }
    override val cRadiusesProperty = FXCollections.observableArrayList(anaglyphStereoscopyModel.cRadiuses)!!
    override val cHeightProperty = bind { anaglyphStereoscopyModel.cHeightProperty }
    override val cHeightsProperty = FXCollections.observableArrayList(anaglyphStereoscopyModel.cHeights)!!
    override val sphereRadiusProperty = bind { anaglyphStereoscopyModel.sphereRadiusProperty }
    override val sphereRadiusesProperty = FXCollections.observableArrayList(anaglyphStereoscopyModel.sphereRadiuses)!!
    override val cuboidEdgeLengthProperty = bind { anaglyphStereoscopyModel.cuboidEdgeLengthProperty }
    override val cuboidEdgeLengthsProperty = FXCollections.observableArrayList(anaglyphStereoscopyModel.cuboidEdgeLengths)!!
    override val meshDensityProperty = bind { anaglyphStereoscopyModel.meshDensityProperty }
    override val meshDensitiesProperty = FXCollections.observableArrayList(anaglyphStereoscopyModel.meshDensities)!!
    private var originalImage: Image? = null

    override val imageProperty = bind { anaglyphStereoscopyModel.imageProperty }

    init {
        val blankImageService = BlankImageService()
        blankImageService.loadImage()
        originalImage = blankImageService.image
        anaglyphStereoscopyModel.onOpenImage(originalImage!!)
    }

    override fun onMouseClick(event: MouseEvent) {
        if (originalImage == null) return
        val coordinate = Coordinate(event.x.toInt(), event.y.toInt())
        if (!isCoordinateInsideImageBounds(coordinate, originalImage!!)) return
        anaglyphStereoscopyModel.addObject(coordinate)
    }

    private fun isCoordinateInsideImageBounds(coordinate: Coordinate, image: Image): Boolean {
        return coordinate.x >= 0 && coordinate.x < image.width &&
                coordinate.y >= 0 && coordinate.y < image.height
    }

    override fun onOpenImage() {
        val image = imageService.image ?: return
        originalImage = image
        anaglyphStereoscopyModel.onOpenImage(image)
    }

    override fun onSaveImage() {
        TODO("implement")
    }

    override fun onResetImage() {
        anaglyphStereoscopyModel.onResetImage()
    }

    override fun onCloseImage() {
        anaglyphStereoscopyModel.onCloseImage()
    }
}
