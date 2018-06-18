package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float3
import javafx.application.Platform
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import javafx.scene.image.PixelWriter
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import model.canvas.Coordinate
import service.BigBlankImageService
import tornadofx.*
import java.util.*
import javax.inject.Inject

class AnaglyphStereoscopyModel @Inject constructor(private val meshDrawingService: MeshDrawingService) {
    val shapes = listOf("Cylinder", "Cone", "Sphere", "Cuboid")
    val cRadiuses = listOf(10, 20, 40, 80)
    val cHeights = listOf(10, 20, 40, 80)
    val sphereRadiuses = listOf(10, 20, 40, 80)
    val cuboidEdgeLengths = listOf(80, 160, 320, 640)
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

    private val meshes = ArrayList<Mesh>()
    private val camera: Camera
    private val whiteColor = Color.color(1.0, 1.0, 1.0)

    init {
        bigBlankImageService.loadImage()
        camera = Camera(
                Float3(105f, 210f, 420f),
//                Float3(420f, 0f, 105f),
                Float3(0f, 0f, 0f),
                Float3(0f, 1f, 0f)
        )
    }

    private fun startMeshDrawingThread() {
        Thread(Runnable {
            while (true) {
                val outputImage = WritableImage(image.width.toInt(), image.height.toInt())
                val pixelWriter = outputImage.pixelWriter
                for (i in 0 until image.width.toInt()) {
                    for (j in 0 until image.height.toInt()) {
                        pixelWriter.setColor(i, j, whiteColor)
                    }
                }
                synchronized(meshes) {
                    for (m in meshes) {
                        drawObject(m, pixelWriter)
                    }
                }
                image = outputImage
                Thread.sleep(1000)
            }
        }).start()
    }

    fun addObject(coordinate: Coordinate) {
        val mesh = when (shape) {
            "Cylinder" -> CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(),
                    coordinate.x.toFloat() - image.width.toFloat() / 2f, coordinate.y.toFloat() - image.height.toFloat() / 2f)
            "Cone" -> CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(),
                    coordinate.x.toFloat() - image.width.toFloat() / 2f, coordinate.y.toFloat() - image.height.toFloat() / 2f)
            "Sphere" -> CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(),
                    coordinate.x.toFloat() - image.width.toFloat() / 2f, coordinate.y.toFloat() - image.height.toFloat() / 2f)
            "Cuboid" -> CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(),
                    coordinate.x.toFloat() - image.width.toFloat() / 2f, coordinate.y.toFloat() - image.height.toFloat() / 2f)
            else -> {
                CuboidMesh(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(),
                        coordinate.x.toFloat() - image.width.toFloat() / 2f, coordinate.y.toFloat() - image.height.toFloat() / 2f)
            }
        }
        synchronized(meshes) {
            meshes.add(mesh)
        }
    }

    private fun drawObject(mesh: Mesh, pixelWriter: PixelWriter) {
        meshDrawingService.draw(mesh, pixelWriter, image.width.toFloat(), image.height.toFloat(), camera)
    }

    fun onOpenImage(openedImage: Image) {
        image = openedImage
        originalImage = openedImage
        startMeshDrawingThread()
    }

    fun onResetImage() {
        image = originalImage
        meshes.clear()
    }

    fun onCloseImage() {
        image = null
        originalImage = null
    }
}