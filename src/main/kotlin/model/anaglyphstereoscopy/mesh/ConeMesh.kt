package model.anaglyphstereoscopy.mesh

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Float4
import com.curiouscreature.kotlin.math.Mat4
import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color
import model.anaglyphstereoscopy.LineDrawer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ConeMesh(radius: Float, height: Float, private val n: Int, initPosition: Float3) : Mesh() {

    private val vertices = arrayOfNulls<Float4>(n + 2)

    init {
        position = Float3(initPosition.x, initPosition.y, initPosition.z)
        //base centre
        vertices[0] = Float4(0f, 0f, 0f, 1f)
        //top vertex
        vertices[n + 1] = Float4(0f, height, 0f, 1f)
        //base perimeter vertices
        (1 until n + 1).forEach {
            vertices[it] = Float4(
                    radius * cos(((2 * PI) / n) * (it - 1)).toFloat(),
                    0f,
                    radius * sin(((2 * PI) / n) * (it - 1)).toFloat(),
                    1f)
        }
    }

    override fun draw(mappingMatrix: Mat4, transformMatrix: Mat4, pixelWriter: PixelWriter,
                      lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float, color: Color) {
        //map to 2D space
        val vs = vertices.map { transformMatrix * mappingMatrix * it!! }.map { it / it.w }

        //base
        (1 until n + 1).forEach {
            drawTriangle(vs[0], vs[it], vs[(it % n) + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
        //side
        (1 until n + 1).forEach {
            drawTriangle(vs[n + 1], vs[it], vs[(it % n) + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
    }

    private fun drawTriangle(v1: Float4, v2: Float4, v3: Float4, pixelWriter: PixelWriter,
                             lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float, color: Color) {
        lineDrawer.drawLine(v1, v2, pixelWriter, imageWidth, imageHeight, color)
        lineDrawer.drawLine(v2, v3, pixelWriter, imageWidth, imageHeight, color)
        lineDrawer.drawLine(v3, v1, pixelWriter, imageWidth, imageHeight, color)
    }
}