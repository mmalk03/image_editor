package service

import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.*
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class ImageFileChooserService : ImageService {
    override fun getImage(): Image? {
        val file = tornadofx.chooseFile("Choose image",
                arrayOf(FileChooser.ExtensionFilter("All files", "*.*"),
                        FileChooser.ExtensionFilter("PNG", "*.png"),
                        FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        FileChooser.ExtensionFilter("BMP", "*.bmp")),
                FileChooserMode.Single).firstOrNull()
        file?.inputStream()?.use {
            return Image(it)
        }
        return null
    }
}