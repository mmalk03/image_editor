package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Float4
import com.curiouscreature.kotlin.math.Mat4
import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color
import java.util.*

class CuboidMesh(dimensions: Float3, initPosition: Float3) : Mesh() {
    private val vertices: Array<Float4>

    init {
        position = Float3(initPosition.x, initPosition.y, initPosition.z)
        vertices = arrayOf(
                //front face
                Float4(-dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
                //right face
                Float4(dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
                //back face
                Float4(dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
                //left face
                Float4(-dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
                //bottom face
                Float4(dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
                //top face
                Float4(-dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
                Float4(-dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
                Float4(dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f))
    }

    override fun draw(mappingMatrix: Mat4, transformMatrix: Mat4, pixelWriter: PixelWriter,
                      lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float) {
        val random = Random()
        val color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())
        var v1 = Float4(0f, 0f, 0f, 1f)
        var v2 = Float4(0f, 0f, 0f, 1f)
        var v3 = Float4(0f, 0f, 0f, 1f)
        var index = 1
        for (t in vertices) {

            val point = transformMatrix * mappingMatrix * t
            point.transform { fl -> fl / point.w }

            when (index) {
                1 -> v1 = point
                2 -> v2 = point
                3 -> {
                    v3 = point
                    lineDrawer.drawLine(v1, v2, pixelWriter, imageWidth, imageHeight, color)
                    lineDrawer.drawLine(v2, v3, pixelWriter, imageWidth, imageHeight, color)
                    lineDrawer.drawLine(v3, v1, pixelWriter, imageWidth, imageHeight, color)
                }
                else -> {
                    lineDrawer.drawLine(v1, v3, pixelWriter, imageWidth, imageHeight, color)
                    lineDrawer.drawLine(v3, point, pixelWriter, imageWidth, imageHeight, color)
                    lineDrawer.drawLine(point, v1, pixelWriter, imageWidth, imageHeight, color)
                    index = 0
                }
            }
            index += 1
        }
    }
}