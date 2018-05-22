package model.flood

import javafx.scene.image.Image
import model.canvas.Coordinate

interface FloodFillingStrategy {
    fun fill(image: Image, startCoordinate: Coordinate): Image
}