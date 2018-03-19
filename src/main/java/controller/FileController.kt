package main.java.controller

import javafx.stage.FileChooser
import tornadofx.Controller
import tornadofx.FileChooserMode
import java.io.File

class FileController : Controller() {

    fun chooseFile(): File? {
        val file = tornadofx.chooseFile("Choose image",
                arrayOf(FileChooser.ExtensionFilter("All files", "*.*"),
                        FileChooser.ExtensionFilter("PNG", "*.png"),
                        FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        FileChooser.ExtensionFilter("BMP", "*.bmp")),
                FileChooserMode.Single).firstOrNull()
        return file
    }
}