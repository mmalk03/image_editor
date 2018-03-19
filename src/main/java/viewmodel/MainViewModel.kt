package main.java.viewmodel

import javafx.collections.FXCollections
import javafx.scene.image.Image
import main.java.controller.FileController
import main.java.model.DoubleImage
import main.java.model.filter.DitheringFilter
import tornadofx.ViewModel
import java.io.FileInputStream

class MainViewModel : ViewModel() {

    val fileController: FileController by inject()
    val ditheringFilter: DitheringFilter by inject()
    val doubleImage = DoubleImage()

    val grayLevel = bind { ditheringFilter.grayLevel }
    val originalImage = bind { doubleImage.originalImage }
    val filteredImage = bind { doubleImage.filteredImage }
    val grayLevels = FXCollections.observableArrayList(2, 4, 6, 8)

    fun onOpenImage() {
        print(ditheringFilter.grayLevel.get())
        val file = fileController.chooseFile()
        file?.inputStream()?.use {
            loadImageFromFile(it)
        }
    }

    fun onSaveImage() {
        TODO("implement")
    }

    fun onResetImage() {
        doubleImage.filteredImage.set(originalImage.value)
    }

    fun onCloseImage() {
        doubleImage.originalImage.set(null)
        doubleImage.filteredImage.set(null)
    }

    private fun loadImageFromFile(fileInputStream: FileInputStream) {
        val image = Image(fileInputStream)
        doubleImage.originalImage.set(image)
        doubleImage.filteredImage.set(image)
    }
}