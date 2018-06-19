package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Float4
import com.curiouscreature.kotlin.math.Mat4
import javafx.scene.image.PixelWriter
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CylinderMesh(radius: Float, height: Float, n: Int, initPosition: Float3) : Mesh() {
    override fun draw(mappingMatrix: Mat4, transformMatrix: Mat4, pixelWriter: PixelWriter, lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val topBaseCenterVertex: Float4
    val topBaseSideVertices: Array<Float4>
    val botBaseCenterVertex: Float4
    val botBaseSideVertices: Array<Float4>

    init {
        position = Float3(initPosition.x, initPosition.y, initPosition.z)
        topBaseCenterVertex = Float4(0f, height, 0f, 1f)
        topBaseSideVertices = Array(n, { i ->
            Float4(
                    radius * cos(((2 * PI) / n) * (i - 1)).toFloat(),
                    height,
                    radius * sin(((2 * PI) / n) * (i - 1)).toFloat(),
                    1f)
        })
        botBaseCenterVertex = Float4(0f, 0f, 0f, 1f)
        botBaseSideVertices = Array(n, { i ->
            Float4(
                    radius * cos(((2 * PI) / n) * (i - 1)).toFloat(),
                    0f,
                    radius * sin(((2 * PI) / n) * (i - 1)).toFloat(),
                    1f)
        })
    }
}