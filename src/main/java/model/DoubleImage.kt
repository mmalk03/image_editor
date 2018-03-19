package main.java.model

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image

class DoubleImage {

    val originalImage = SimpleObjectProperty<Image>()
    var filteredImage = SimpleObjectProperty<Image>()
}