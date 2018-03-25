package viewmodel

import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.scene.image.Image
import model.DoubleImage
import model.filter.ImageFilter
import model.filter.dithering.AverageDitheringFilterStrategy
import model.filter.dithering.OrderedDitheringFilterStrategy
import model.filter.dithering.RandomDitheringFilterStrategy
import model.filter.grayscale.ScientificGrayscaleFilterStrategy
import model.filter.quantization.MedianCutFilterStrategy
import service.ImageService
import tornadofx.*

class MainViewModel : ViewModel() {

    val imageService: ImageService by di()
    val imageFilter = ImageFilter()
    val doubleImage = DoubleImage()

    val grayLevel = SimpleIntegerProperty(2)
    val grayLevels = FXCollections.observableArrayList(2, 4, 6, 8)

    val quantizationColorLevel = SimpleIntegerProperty(8)
    val quantizationColorLevels = FXCollections.observableArrayList(2, 4, 8, 16, 32, 64, 128, 256)

    val ditherMatrixDimension = SimpleIntegerProperty(2)
    val ditherMatrixDimensions = FXCollections.observableArrayList(2, 3, 4, 6)

    val leftImage = bind { doubleImage.leftImage }
    val rightImage = bind { doubleImage.rightImage }
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
                        AverageDitheringFilterStrategy(grayLevel.value)
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

    private fun setLeftToGrayscale() {
        leftImage.value = imageFilter.filter(
                leftImage.value,
                ScientificGrayscaleFilterStrategy()
        )
    }

    private fun setLeftToOriginal() {
        leftImage.value = originalImage
    }

    fun onOpenImage() {
        val image = imageService.getImage()
        leftImage.value = image
        rightImage.value = image
        originalImage = image
    }

    fun onSaveImage() {
        TODO("implement")
    }

    fun onResetImage() {
        leftImage.value = originalImage
        rightImage.value = originalImage
    }

    fun onCloseImage() {
        leftImage.value = null
        rightImage.value = null
        originalImage = null
    }
}