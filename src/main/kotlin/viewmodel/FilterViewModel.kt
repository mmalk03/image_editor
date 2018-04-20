package viewmodel

import com.google.inject.Inject
import javafx.beans.property.Property
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.image.Image
import model.filter.FilterModel
import service.ImageService
import tornadofx.*

abstract class IFilterViewModel : MyViewModel() {
    abstract val leftImage: Property<Image>
    abstract val rightImage: Property<Image>
    abstract val grayLevelProperty: Property<Number>
    abstract val quantizationColorLevelProperty: Property<Number>
    abstract val ditherMatrixDimensionProperty: Property<Number>
    abstract val grayLevelsProperty: ObservableList<Int>
    abstract val quantizationColorLevelsProperty: ObservableList<Int>
    abstract val ditherMatrixDimensionsProperty: ObservableList<Int>
    abstract val ditheringRandomCommand: Command<Any>
    abstract val ditheringAverageCommand: Command<Any>
    abstract val ditheringOrderedCommand: Command<Any>
    abstract val quantizationMedianCutCommand: Command<Any>
    abstract val quantizationPopularityCommand: Command<Any>
}

class FilterViewModel @Inject constructor(private val imageService: ImageService,
                                          private val filterModel: FilterModel) : IFilterViewModel() {
    override val leftImage = bind { filterModel.leftImageProperty }
    override val rightImage = bind { filterModel.rightImageProperty }

    override val grayLevelProperty = bind { filterModel.grayLevelProperty }
    override val quantizationColorLevelProperty = bind { filterModel.quantizationColorLevelProperty }
    override val ditherMatrixDimensionProperty = bind { filterModel.ditherMatrixDimensionProperty }

    override val grayLevelsProperty = FXCollections.observableArrayList(filterModel.grayLevels)!!
    override val quantizationColorLevelsProperty = FXCollections.observableArrayList(filterModel.quantizationColorLevels)!!
    override val ditherMatrixDimensionsProperty = FXCollections.observableArrayList(filterModel.ditherMatrixDimensions)!!

    override val ditheringRandomCommand = command {
        runAsync {
            filterModel.ditheringRandom()
        }
    }
    override val ditheringAverageCommand = command {
        runAsync {
            filterModel.ditheringAverage()
        }
    }
    override val ditheringOrderedCommand = command {
        runAsync {
            filterModel.ditheringOrdered()
        }
    }
    override val quantizationMedianCutCommand = command {
        runAsync {
            filterModel.quantizationMedianCut()
        }
    }
    override val quantizationPopularityCommand = command {
        runAsync {
            filterModel.quantizationPopularity()
        }
    }

    override fun onOpenImage() {
        val image = imageService.image
        if (image != null) {
            filterModel.onOpenImage(image)
        }
    }

    override fun onSaveImage() {
        TODO("implement")
    }

    override fun onResetImage() {
        filterModel.onResetImage()
    }

    override fun onCloseImage() {
        filterModel.onCloseImage()
    }
}