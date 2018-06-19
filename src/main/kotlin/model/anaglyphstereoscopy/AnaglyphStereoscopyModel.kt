package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float3
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import javafx.scene.image.PixelWriter
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import model.anaglyphstereoscopy.mesh.*
import model.canvas.Coordinate
import service.BigBlankImageService
import tornadofx.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class AnaglyphStereoscopyModel @Inject constructor(private val meshDrawingService: MeshDrawingService) {
    val shapes = listOf("Cylinder", "Cone", "Sphere", "Cuboid")
    val cRadiuses = listOf(40, 80, 160, 320)
    val cHeights = listOf(40, 80, 160, 320)
    val sphereRadiuses = listOf(40, 80, 160, 320)
    val cuboidEdgeLengths = listOf(80, 160, 240, 320)
    val meshDensities = listOf(10, 20, 40, 80, 160)

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

    private val meshes = HashMap<Mesh, Pair<Float3, Float3>>()
    private val camera: Camera
    private val whiteColor = Color.color(1.0, 1.0, 1.0)
    private val random = Random()
    private val maxTranslation = 0f
    private val minTranslation = 0f
    private val maxRotation = 0f
    private val minRotation = 0f
    private val drawingDelay = 10L
    private val cameraMovement = 20f

    init {
        bigBlankImageService.loadImage()
        camera = Camera(
                Float3(0f, 0f, 0f),
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
                        if (m.key.position.x < -image.width / 2 || m.key.position.x > image.width / 2) {
                            m.value.first.x *= -1
                        }
                        if (m.key.position.y < -image.height / 2 || m.key.position.y > image.height / 2) {
                            m.value.first.y *= -1
                        }
                        m.key.moveBy(m.value.first)
                        m.key.rotateBy(m.value.second)
                        drawObject(m.key, pixelWriter)
                    }
                }
                image = outputImage
                Thread.sleep(drawingDelay)
            }
        }).start()
    }

    fun addObject(coordinate: Coordinate) {
        val mesh = when (shape) {
            "Cylinder" -> getCylinderMesh(coordinate.x, coordinate.y)
            "Cone" -> getConeMesh(coordinate.x, coordinate.y)
            "Sphere" -> getSphereMesh(coordinate.x, coordinate.y)
            "Cuboid" -> getCuboidMesh(coordinate.x, coordinate.y)
            else -> {
                throw RuntimeException("Something went wrong")
            }
        }
        synchronized(meshes) {
            meshes.put(mesh, Pair(getRandomTranslationVector(), getRandomRotationVector()))
        }
    }

    fun onLeftClick() {
        synchronized(camera) { camera.position.x -= cameraMovement }
    }

    fun onRightClick() {
        synchronized(camera) { camera.position.x += cameraMovement }
    }

    fun onUpClick() {
        synchronized(camera) { camera.position.y += cameraMovement }
    }

    fun onDownClick() {
        synchronized(camera) { camera.position.y -= cameraMovement }
    }

    fun onForwardClick() {
        synchronized(camera) { camera.position.z -= cameraMovement }
    }

    fun onBackwardClick() {
        synchronized(camera) { camera.position.z += cameraMovement }
    }

    private fun drawObject(mesh: Mesh, pixelWriter: PixelWriter) {
        meshDrawingService.draw(mesh, pixelWriter, image.width.toFloat(), image.height.toFloat(), camera)
    }

    private fun getCuboidMesh(x: Int, y: Int): CuboidMesh {
        return CuboidMesh(
                Float3(cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat(), cuboidEdgeLength.toFloat()),
                Float3(x.toFloat() - image.width.toFloat() / 2f, y.toFloat() - image.height.toFloat() / 2f, 0f))
    }

    private fun getCylinderMesh(x: Int, y: Int): CylinderMesh {
        return CylinderMesh(cRadius.toFloat(), cHeight.toFloat(), meshDensity,
                Float3(x.toFloat() - image.width.toFloat() / 2f, y.toFloat() - image.height.toFloat() / 2f, 0f))
    }

    private fun getConeMesh(x: Int, y: Int): ConeMesh {
        return ConeMesh(cRadius.toFloat(), cHeight.toFloat(), meshDensity,
                Float3(x.toFloat() - image.width.toFloat() / 2f, y.toFloat() - image.height.toFloat() / 2f, 0f))
    }

    private fun getSphereMesh(x: Int, y: Int): SphereMesh {
        return SphereMesh(sphereRadius.toFloat(), meshDensity,
                Float3(x.toFloat() - image.width.toFloat() / 2f, y.toFloat() - image.height.toFloat() / 2f, 0f))
    }

    private fun getRandomTranslationVector(): Float3 {
        return Float3(
                random.nextFloat() * (maxTranslation - minTranslation) + minTranslation,
                random.nextFloat() * (maxTranslation - minTranslation) + minTranslation,
                random.nextFloat() * (maxTranslation - minTranslation) + minTranslation)
    }

    private fun getRandomRotationVector(): Float3 {
        return Float3(
                random.nextFloat() * (maxRotation - minRotation) + minRotation,
                random.nextFloat() * (maxRotation - minRotation) + minRotation,
                random.nextFloat() * (maxRotation - minRotation) + minRotation)
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