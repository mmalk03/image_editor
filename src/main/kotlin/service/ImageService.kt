package service

import javafx.scene.image.Image

abstract class ImageService {
    abstract var image: Image?
    abstract fun loadImage()
}