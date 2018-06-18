package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float3
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
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

    val shapeProperty = SimpleStringProperty(shapes.last())
    private var shape by shapeProperty
    val cRadiusProperty = SimpleIntegerProperty(cRadiuses.first())
    private var cRadius by cRadiusProperty
    val cHeightProperty = SimpleIntegerProperty(cHeights.first())
    private var cHeight by cHeightProperty
    val sphereRadiusProperty = SimpleIntegerProperty(sphereRadiuses.first())
    private var sphereRadius by sphereRadiusProperty
    val cuboidEdgeLengthProperty = SimpleIntegerProperty(cuboidEdgeLengths.last())
    private var cuboidEdgeLength by cuboidEdgeLengthProperty
    val meshDensityProperty = SimpleIntegerProperty(meshDensities.first())
    private var meshDensity by meshDensityProperty

    init {
        bigBlankImageService.loadImage()
    }

    fun addObject(coordinate: Coordinate) {
        when (shape) {
            "Cylinder" -> drawObject(CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat()))
            "Cone" -> drawObject(CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat()))
            "Sphere" -> drawObject(CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat()))
            "Cuboid" -> drawObject(CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat()))
        }
    }

    private fun drawObject(mesh: Mesh) {
        val camera = Camera(
                Float3(image.width.toFloat() / 2f, image.height.toFloat() / 2f, 210f),
                Float3(0f, 0f, 0f),
//                Float3(image.width.toFloat() / 2f, image.height.toFloat() / 2f, 0f),
                Float3(0f, 1f, 0f)
        )
        val outputImage = WritableImage(image.width.toInt(), image.height.toInt())
        val pixelWriter = outputImage.pixelWriter
        val pixelReader = image.pixelReader

        for (i in 0 until image.width.toInt()) {
            for (j in 0 until image.height.toInt()) {
                pixelWriter.setColor(i, j, pixelReader.getColor(i, j))
            }
        }
        mesh.draw(pixelWriter, image.width.toFloat(), image.height.toFloat(), camera)
        image = outputImage
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