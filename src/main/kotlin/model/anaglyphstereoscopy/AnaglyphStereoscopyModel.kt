package model.anaglyphstereoscopy

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import model.canvas.Coordinate
import service.BigBlankImageService
import tornadofx.*

class AnaglyphStereoscopyModel {
    val shapes = listOf("Cylinder", "Cone", "Sphere", "Cuboid")
    val cRadiuses = listOf(10, 20, 40, 80)
    val cHeights = listOf(10, 20, 40, 80)
    val sphereRadiuses = listOf(10, 20, 40, 80)
    val cuboidEdgeLengths = listOf(10, 20, 40, 80)
    val meshDensities = listOf(10, 20, 40, 80)

    private var originalImage: Image? = null
    private val bigBlankImageService = BigBlankImageService()

    val imageProperty = SimpleObjectProperty<Image>()
    private var image by imageProperty

    val shapeProperty = SimpleStringProperty(shapes.first())
    private var shape by shapeProperty
    val cRadiusProperty = SimpleIntegerProperty(cRadiuses.first())
    private var cRadius by cRadiusProperty
    val cHeightProperty = SimpleIntegerProperty(cHeights.first())
    private var cHeight by cHeightProperty
    val sphereRadiusProperty = SimpleIntegerProperty(sphereRadiuses.first())
    private var sphereRadius by sphereRadiusProperty
    val cuboidEdgeLengthProperty = SimpleIntegerProperty(cuboidEdgeLengths.first())
    private var cuboidEdgeLength by cuboidEdgeLengthProperty
    val meshDensityProperty = SimpleIntegerProperty(meshDensities.first())
    private var meshDensity by meshDensityProperty

    init {
        bigBlankImageService.loadImage()
    }

    fun addObject(coordinate: Coordinate){
        when(shape){
            "Cylinder" -> drawObject(CuboidMesh())
            "Cone" -> drawObject(CuboidMesh())
            "Sphere" -> drawObject(CuboidMesh())
            "Cuboid" -> drawObject(CuboidMesh())
        }
    }

    private fun drawObject(mesh: Mesh){

        //TODO: draw mesh
    }

    fun onOpenImage(openedImage: Image) {
        image = openedImage
        originalImage = openedImage
    }

    fun onResetImage() {
        image = originalImage
    }

    fun onCloseImage() {
        image = null
        originalImage = null
    }
}