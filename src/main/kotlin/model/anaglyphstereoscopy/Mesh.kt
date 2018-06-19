package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Mat4
import javafx.scene.image.PixelWriter

abstract class Mesh {
    //    abstract val vertices: Array<Float4>
    var position = Float3(0f, 0f, 0f)
    var rotation = Float3(0f, 0f, 0f)

    fun moveBy(delta: Float3) {
        position.x += delta.x
        position.y += delta.y
        position.z += delta.z
    }

    fun rotateBy(delta: Float3) {
        rotation.x += delta.x
        rotation.y += delta.y
        rotation.z += delta.z
    }

    abstract fun draw(mappingMatrix: Mat4, transformMatrix: Mat4, pixelWriter: PixelWriter,
                      lineDrawer: LineDrawer, imageWidth: Float, imageHeight: Float)
}