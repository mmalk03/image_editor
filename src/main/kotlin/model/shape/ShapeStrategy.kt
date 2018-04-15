package model.shape

import javafx.scene.image.Image

abstract class ShapeStrategy {
    abstract fun getCoordinates(image: Image): List<Pair<Int, Int>>
}