package model

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image

class DoubleImage {
    var leftImage = SimpleObjectProperty<Image>()
    var rightImage = SimpleObjectProperty<Image>()
}