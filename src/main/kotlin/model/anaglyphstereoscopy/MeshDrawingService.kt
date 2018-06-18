package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float4
import com.google.inject.Inject
import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color
import model.canvas.linedrawing.OptimalLineStrategy
import java.util.*

class MeshDrawingService @Inject constructor(private val meshMatrixFactory: MeshMatrixFactory,
                                             private val optimalLineStrategy: OptimalLineStrategy) {

    private val random = Random()

    fun draw(mesh: Mesh, pixelWriter: PixelWriter, imageWidth: Float, imageHeight: Float, camera: Camera) {
        val color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())

        val cameraMatrix = meshMatrixFactory.getCameraMatrix(camera)
        val projectionMatrix = meshMatrixFactory.getProjectionMatrix(90f, imageWidth, imageHeight)
        val mappingMatrix = projectionMatrix * cameraMatrix

        val translationMatrix = meshMatrixFactory.getTranslationMatrix(mesh.position)
        val rotationXMatrix = meshMatrixFactory.getRotationMatrixX(mesh.rotation.x)
        val rotationYMatrix = meshMatrixFactory.getRotationMatrixX(mesh.rotation.y)
        val rotationZMatrix = meshMatrixFactory.getRotationMatrixX(mesh.rotation.z)
        val transformMatrix = translationMatrix * rotationXMatrix * rotationYMatrix * rotationZMatrix

        var v1 = Float4(0f, 0f, 0f, 1f)
        var v2 = Float4(0f, 0f, 0f, 1f)
        var v3 = Float4(0f, 0f, 0f, 1f)
        var index = 1
        for (t in mesh.vertices) {

            val point = transformMatrix * mappingMatrix * t
            point.transform { fl -> fl / point.w }

            when (index) {
                1 -> v1 = point
                2 -> v2 = point
                3 -> {
                    v3 = point
                    drawLine(v1, v2, pixelWriter, imageWidth, imageHeight, color)
                    drawLine(v2, v3, pixelWriter, imageWidth, imageHeight, color)
                    drawLine(v3, v1, pixelWriter, imageWidth, imageHeight, color)
                }
                else -> {
                    drawLine(v1, v3, pixelWriter, imageWidth, imageHeight, color)
                    drawLine(v3, point, pixelWriter, imageWidth, imageHeight, color)
                    drawLine(point, v1, pixelWriter, imageWidth, imageHeight, color)
                    index = 0
                }
            }
            index += 1
        }
    }

    private fun drawLine(v1: Float4, v2: Float4, pixelWriter: PixelWriter, imageWidth: Float, imageHeight: Float, color: Color) {
        optimalLineStrategy.drawLine(v1.x.toInt(), v1.y.toInt(), v2.x.toInt(), v2.y.toInt(), imageWidth.toInt(), imageHeight.toInt(), pixelWriter, color)
    }
}