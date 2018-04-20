package model.filter

import com.google.inject.Inject
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import model.filter.dithering.AverageDitheringFilterStrategy
import model.filter.dithering.OrderedDitheringFilterStrategy
import model.filter.dithering.RandomDitheringFilterStrategy
import model.filter.grayscale.GrayscaleFilterStrategy
import model.filter.quantization.MedianCutFilterStrategy
import model.filter.quantization.PopularityFilterStrategy
import tornadofx.*

class FilterModel @Inject constructor(private val grayscaleFilterStrategy: GrayscaleFilterStrategy,
                                      private val imageFilter: IImageFilter) {
    val grayLevels = listOf(2, 4, 6, 8)
    val quantizationColorLevels = listOf(2, 4, 8, 16, 32, 64, 128, 256)
    val ditherMatrixDimensions = listOf(2, 3, 4, 6)

    private var originalImage: Image? = null

    val leftImageProperty = SimpleObjectProperty<Image>()
    private var leftImage by leftImageProperty

    val rightImageProperty = SimpleObjectProperty<Image>()
    private var rightImage by rightImageProperty

    val grayLevelProperty = SimpleIntegerProperty(2)
    private var grayLevel by grayLevelProperty

    val quantizationColorLevelProperty = SimpleIntegerProperty(8)
    private var quantizationColorLevel by quantizationColorLevelProperty

    val ditherMatrixDimensionProperty = SimpleIntegerProperty(2)
    private var ditherMatrixDimension by ditherMatrixDimensionProperty

    fun ditheringRandom() {
        if (originalImage == null) return
        setLeftToGreyscale()
        rightImage = imageFilter.filter(originalImage!!, RandomDitheringFilterStrategy(grayLevel))
    }

    fun ditheringAverage() {
        if (originalImage == null) return
        setLeftToGreyscale()
        rightImage = imageFilter.filter(originalImage!!, AverageDitheringFilterStrategy(originalImage!!, grayLevel))
    }

    fun ditheringOrdered() {
        if (originalImage == null) return
        setLeftToGreyscale()
        rightImage = imageFilter.filter(originalImage!!, OrderedDitheringFilterStrategy(grayLevel, ditherMatrixDimension))
    }

    fun quantizationMedianCut() {
        if (originalImage == null) return
        setLeftToOriginal()
        rightImage = imageFilter.filter(originalImage!!, MedianCutFilterStrategy(originalImage!!, quantizationColorLevel))
    }

    fun quantizationPopularity() {
        if (originalImage == null) return
        setLeftToOriginal()
        rightImage = imageFilter.filter(originalImage!!, PopularityFilterStrategy(originalImage!!, quantizationColorLevel))
    }

    fun onOpenImage(image: Image) {
        leftImage = image
        rightImage = image
        originalImage = image
    }

    fun onResetImage() {
        leftImage = originalImage
        rightImage = originalImage
    }

    fun onCloseImage() {
        leftImage = null
        rightImage = null
        originalImage = null
    }

    private fun setLeftToGreyscale() {
        leftImage = imageFilter.filter(leftImage, grayscaleFilterStrategy)
    }

    private fun setLeftToOriginal() {
        leftImage = originalImage
    }
}