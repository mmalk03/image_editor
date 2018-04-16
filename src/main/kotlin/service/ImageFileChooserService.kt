package service

import com.google.inject.Singleton
import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.*

@Singleton
class ImageFileChooserService : ImageService() {
    override var image: Image? = null

    override fun loadImage() {
        val file = tornadofx.chooseFile("Choose image",
                arrayOf(FileChooser.ExtensionFilter("All files", "*.*"),
                        FileChooser.ExtensionFilter("PNG", "*.png"),
                        FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        FileChooser.ExtensionFilter("BMP", "*.bmp")),
                FileChooserMode.Single).firstOrNull()
        file?.inputStream()?.use {
            image = Image(it)
        }
    }
}