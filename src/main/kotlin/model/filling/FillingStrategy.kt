package model.filling

import javafx.scene.image.Image

interface FillingStrategy {
    fun fillPolygon(polygon: Polygon, image: Image, colorStrategy: ColorStrategy): Image
}