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

class CylinderMesh(radius: Float, height: Float, private val n: Int, initPosition: Float3) : Mesh() {

    private val vertices: Array<Float4?>

    init {
        position = Float3(initPosition.x, initPosition.y, initPosition.z)
        vertices = arrayOfNulls(4 * n + 2)
        //top base
        vertices[0] = Float4(0f, height, 0f, 1f)
        (1 until n + 1).forEach {
            vertices[it] = Float4(
                    radius * cos(((2 * PI) / n) * (it - 1)).toFloat(),
                    height,
                    radius * sin(((2 * PI) / n) * (it - 1)).toFloat(),
                    1f)
        }
        //bottom base
        vertices[4 * n + 1] = Float4(0f, 0f, 0f, 1f)
        (3 * n + 1 until 4 * n + 1).forEach {
            vertices[it] = Float4(
                    radius * cos(((2 * PI) / n) * (it - 1)).toFloat(),
                    0f,
                    radius * sin(((2 * PI) / n) * (it - 1)).toFloat(),
                    1f)
        }
        //side
        (n + 1 until 2 * n + 1).forEach {
            vertices[it] = vertices[it - n]
        }
        (2 * n + 1 until 3 * n + 1).forEach {
            vertices[it] = vertices[it + n]
        }
    }

    override fun draw(mappingMatrix: Mat4, transformMatrix: Mat4, pixelWriter: PixelWriter,
                      lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float, color: Color) {
        //map to 2D space
        val vs = vertices.map { transformMatrix * mappingMatrix * it!! }.map { it / it.w }

        //top base
        (0 until n).forEach {
            drawTriangle(vs[0], vs[(it + 2) % (n + 1)], vs[it + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
        //bottom base
        (3 * n until 4 * n - 1).forEach {
            drawTriangle(vs[4 * n + 1], vs[it + 2], vs[it + 2], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
        drawTriangle(vs[4 * n + 1], vs[4 * n], vs[3 * n + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        //side
        (n until 2 * n - 1).forEach {
            drawTriangle(vs[it + 1], vs[it + 2], vs[it + 1 + n], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
        drawTriangle(vs[2 * n], vs[n + 1], vs[3 * n], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        (2 * n until 3 * n - 1).forEach {
            drawTriangle(vs[it + 1], vs[it + 2 - n], vs[it + 2], pixelWriter, lineDrawer, imageWidth, imageHeight, color)
        }
        drawTriangle(vs[3 * n], vs[n + 1], vs[2 * n + 1], pixelWriter, lineDrawer, imageWidth, imageHeight, color)

    }

    private fun drawTriangle(v1: Float4, v2: Float4, v3: Float4, pixelWriter: PixelWriter,
                             lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float, color: Color) {
        lineDrawer.drawLine(v1, v2, pixelWriter, imageWidth, imageHeight, color)
        lineDrawer.drawLine(v2, v3, pixelWriter, imageWidth, imageHeight, color)
        lineDrawer.drawLine(v3, v1, pixelWriter, imageWidth, imageHeight, color)
    }
}