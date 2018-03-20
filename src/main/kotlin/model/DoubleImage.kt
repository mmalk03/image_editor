package model

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image

class DoubleImage {

    var originalImage = SimpleObjectProperty<Image>()
    var filteredImage = SimpleObjectProperty<Image>()
}