package model.canvas

import javafx.scene.image.Image

abstract class ShapeStrategy {
    abstract fun getCoordinates(image: Image): List<Coordinate>
}