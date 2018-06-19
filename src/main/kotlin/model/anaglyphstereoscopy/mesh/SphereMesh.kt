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

class SphereMesh(r: Float, private val n: Int, initPosition: Float3) : Mesh() {

    private val vertices: Array<Float4?>
    private val m: Int = n

    init {
        position = Float3(initPosition.x, initPosition.y, initPosition.z)
        vertices = arrayOfNulls(m * n + 2)
        //top pole
        vertices[0] = Float4(0f, r, 0f, 1f)
        //bottom pole
        vertices[m * n + 1] = Float4(0f, -r, 0f, 1f)
        //side
        for (i in 0 until n) {
            for (j in 1 until m + 1) {
                vertices[i * m + j] = Float4(
                        (r * cos(((2 * PI) / m) * (j - 1)) * sin((PI / (n + 1)) * i)).toFloat(),
                        r * cos((PI / (n + 1)) * i).toFloat(),
                        (r * sin(((2 * PI) / m) * (j - 1)) * sin((PI / (n + 1)) * i)).toFloat(),
                        1f
                )
            }
        }
    }

    override fun draw(mappingMatrix: Mat4, transformMatrix: Mat4, pixelWriter: PixelWriter,
                      lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float, color: Color) {
        //map to 2D space
        val vs = vertices.map { transformMatrix * mappingMatrix * it!! }.map { it / it.w }

        //top and bottom lids
        (0 until m - 1).forEach {
            drawTriangle(vs[0], vs[it + 2], vs[it + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
        drawTriangle(vs[0], vs[1], vs[m], pixelWriter, lineDrawer, imageWidth, imageHeight, color)

        (0 until m - 1).forEach {
            drawTriangle(vs[m * n + 1], vs[(n - 1) * m + it + 1], vs[(n - 1) * m + it + 2], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
        drawTriangle(vs[m * n + 1], vs[m * n], vs[(n - 1) * m + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)

        //rings making up the strips between parallels
        for (i in 0 until n - 1) {
            for (j in 1 until m) {
                drawTriangle(vs[i * m + j], vs[i * m + j + 1], vs[(i + 1) * m + j + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
            }
            drawTriangle(vs[(i + 1) * m], vs[i * m + 1], vs[(i + 1) * m + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
            for (j in 1 until m) {
                drawTriangle(vs[i * m + j], vs[(i + 1) * m + j + 1], vs[(i + 1) * m + j], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
            }
            drawTriangle(vs[(i + 1) * m], vs[(i + 1) * m + 1], vs[(i + 2) * m], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
    }

    private fun drawTriangle(v1: Float4, v2: Float4, v3: Float4, pixelWriter: PixelWriter,
                             lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float, color: Color) {
        lineDrawer.drawLine(v1, v2, pixelWriter, imageWidth, imageHeight, color)
        lineDrawer.drawLine(v2, v3, pixelWriter, imageWidth, imageHeight, color)
        lineDrawer.drawLine(v3, v1, pixelWriter, imageWidth, imageHeight, color)
    }
}