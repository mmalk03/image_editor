package viewmodel

import com.authzee.kotlinguice4.getInstance
import com.google.inject.Guice
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import model.DoubleImage
import model.filter.ImageFilter
import model.filter.dithering.AverageDitheringFilterStrategy
import model.filter.dithering.OrderedDitheringFilterStrategy
import model.filter.dithering.RandomDitheringFilterStrategy
import model.filter.grayscale.ScientificGrayscaleFilterStrategy
import model.filter.quantization.MedianCutFilterStrategy
import model.filter.quantization.PopularityFilterStrategy
import model.shape.CircleStrategy
import model.shape.LineStrategy
import model.shape.ShapeDrawer
import module.MainModule
import service.ImageService
import tornadofx.*

class MainViewModel : ViewModel() {

    val imageService: ImageService by di()
    val imageFilter = ImageFilter()
    val doubleImage = DoubleImage()
    val canvasImageProperty = SimpleObjectProperty<Image>()

    val grayLevel = SimpleIntegerProperty(2)
    val grayLevels = FXCollections.observableArrayList(2, 4, 6, 8)

    val quantizationColorLevel = SimpleIntegerProperty(8)
    val quantizationColorLevels = FXCollections.observableArrayList(2, 4, 8, 16, 32, 64, 128, 256)

    val ditherMatrixDimension = SimpleIntegerProperty(2)
    val ditherMatrixDimensions = FXCollections.observableArrayList(2, 3, 4, 6)

    val circleRadius = SimpleIntegerProperty(10)
    val circleRadiuses = FXCollections.observableArrayList(10, 20, 40, 80)

    var firstClickMade = false
    lateinit var firstMouseCoordinate: Pair<Int, Int>
    lateinit var secondMouseCoordinate: Pair<Int, Int>
    val injector = Guice.createInjector(MainModule())
    private val lineStrategy = injector.getInstance<LineStrategy>()
    private val circleStrategy = injector.getInstance<CircleStrategy>()
    val shapeDrawer = ShapeDrawer()

    val leftImage = bind { doubleImage.leftImage }
    val rightImage = bind { doubleImage.rightImage }
    val canvasImage = bind { canvasImageProperty }
    private var originalImage: Image? = null

    val ditheringRandomCommand = command {
        if (originalImage != null) {
            runAsync {
                setLeftToGrayscale()
                rightImage.value = imageFilter.filter(
                        originalImage!!,
                        RandomDitheringFilterStrategy(grayLevel.value)
                )
            }
        }
    }
    val ditheringAverageCommand = command {
        if (originalImage != null) {
            runAsync {
                setLeftToGrayscale()
                rightImage.value = imageFilter.filter(
                        originalImage!!,
                        AverageDitheringFilterStrategy(originalImage!!, grayLevel.value)
                )
            }
        }
    }
    val ditheringOrderedCommand = command {
        if (originalImage != null) {
            runAsync {
                setLeftToGrayscale()
                rightImage.value = imageFilter.filter(
                        originalImage!!,
                        OrderedDitheringFilterStrategy(grayLevel.value, ditherMatrixDimension.value)
                )
            }
        }
    }
    val quantizationMedianCutCommand = command {
        if (originalImage != null) {
            runAsync {
                setLeftToOriginal()
                rightImage.value = imageFilter.filter(
                        originalImage!!,
                        MedianCutFilterStrategy(originalImage!!, quantizationColorLevel.value)
                )
            }
        }
    }
    val quantizationPopularityCommand = command {
        if (originalImage != null) {
            runAsync {
                setLeftToOriginal()
                rightImage.value = imageFilter.filter(
                        originalImage!!,
                        PopularityFilterStrategy(originalImage!!, quantizationColorLevel.value)
                )
            }
        }
    }

    private fun setLeftToGrayscale() {
        leftImage.value = imageFilter.filter(
                leftImage.value,
                ScientificGrayscaleFilterStrategy()
        )
    }

    private fun setLeftToOriginal() {
        leftImage.value = originalImage
    }

    fun onCanvasMouseClick(event: MouseEvent){
        if(firstClickMade){
            secondMouseCoordinate = Pair(event.sceneX.toInt(), event.sceneY.toInt())
            val coordinates = lineStrategy.getCoordinates(
                    firstMouseCoordinate.first,
                    firstMouseCoordinate.second,
                    secondMouseCoordinate.first,
                    secondMouseCoordinate.second)
            canvasImage.value = shapeDrawer.draw(canvasImage.value, coordinates)
        }
        else{
            firstMouseCoordinate = Pair(event.sceneX.toInt(), event.sceneY.toInt())
        }
        firstClickMade = !firstClickMade
    }

    fun onOpenImage() {
        val image = imageService.getImage()
        leftImage.value = image
        rightImage.value = image
        canvasImage.value = image
        originalImage = image
    }

    fun onSaveImage() {
        TODO("implement")
    }

    fun onResetImage() {
        leftImage.value = originalImage
        rightImage.value = originalImage
        canvasImage.value = originalImage
    }

    fun onCloseImage() {
        leftImage.value = null
        rightImage.value = null
        canvasImage.value = null
        originalImage = null
    }
}