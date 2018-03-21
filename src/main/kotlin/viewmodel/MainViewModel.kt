package viewmodel

import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import model.DoubleImage
import model.filter.ImageFilter
import model.filter.dithering.AverageDitheringFilterStrategy
import model.filter.dithering.OrderedDitheringFilterStrategy
import model.filter.dithering.RandomDitheringFilterStrategy
import service.ImageService
import tornadofx.*

class MainViewModel : ViewModel() {

    val imageService: ImageService by di()
    val imageFilter = ImageFilter()

    val doubleImage = DoubleImage()

    val grayLevel = SimpleIntegerProperty(2)
    val grayLevels = FXCollections.observableArrayList(2, 4, 6, 8)
    val ditherMatrixDimension = SimpleIntegerProperty(2)
    val ditherMatrixDimensions = FXCollections.observableArrayList(2, 3, 4, 6)

    val originalImage = bind { doubleImage.originalImage }
    val filteredImage = bind { doubleImage.filteredImage }

    val ditheringRandomCommand = command {
        runAsync {
            filteredImage.value = imageFilter.filter(
                    filteredImage.value,
                    RandomDitheringFilterStrategy(grayLevel.value)
            )
        }
    }
    val ditheringAverageCommand = command {
        runAsync {
            filteredImage.value = imageFilter.filter(
                    filteredImage.value,
                    AverageDitheringFilterStrategy(grayLevel.value)
            )
        }
    }
    val ditheringOrderedCommand = command {
        runAsync {
            filteredImage.value = imageFilter.filter(
                    filteredImage.value,
                    OrderedDitheringFilterStrategy(grayLevel.value, ditherMatrixDimension.value)
            )
        }
    }

    fun onOpenImage() {
        val image = imageService.getImage()
        originalImage.value = image
        filteredImage.value = image
    }

    fun onSaveImage() {
        TODO("implement")
    }

    fun onResetImage() {
        filteredImage.value = originalImage.value
    }

    fun onCloseImage() {
        originalImage.value = null
        filteredImage.value = null
    }
}