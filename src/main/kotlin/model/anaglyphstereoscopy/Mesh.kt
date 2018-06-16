package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.*
import javafx.scene.image.PixelWriter
import kotlin.math.cos
import kotlin.math.sin

abstract class Mesh {

    companion object {
        fun getRotationMatrixX(alpha: Float): Mat4 {
            val sinAlpha = sin(alpha)
            val cosAlpha = cos(alpha)
            return Mat4(
                    Float4(1f, 0f, 0f, 0f),
                    Float4(0f, cosAlpha, -sinAlpha, 0f),
                    Float4(0f, sinAlpha, cosAlpha, 0f),
                    Float4(0f, 0f, 0f, 1f))
        }

        fun getRotationMatrixY(alpha: Float): Mat4 {
            val sinAlpha = sin(alpha)
            val cosAlpha = cos(alpha)
            return Mat4(
                    Float4(cosAlpha, 0f, sinAlpha, 0f),
                    Float4(0f, 1f, 0f, 0f),
                    Float4(-sinAlpha, 0f, cosAlpha, 0f),
                    Float4(0f, 0f, 0f, 1f))
        }

        fun getRotationMatrixZ(alpha: Float): Mat4 {
            val sinAlpha = sin(alpha)
            val cosAlpha = cos(alpha)
            return Mat4(
                    Float4(cosAlpha, -sinAlpha, 0f, 0f),
                    Float4(sinAlpha, cosAlpha, 0f, 0f),
                    Float4(0f, 0f, 1f, 0f),
                    Float4(0f, 0f, 0f, 1f))
        }

        fun getTranslationMatrix(tx: Float, ty: Float, tz: Float): Mat4 {
            return Mat4(
                    Float4(1f, 0f, 0f, tx),
                    Float4(0f, 1f, 0f, ty),
                    Float4(0f, 0f, 0f, tz),
                    Float4(0f, 0f, 0f, 1f)
            )
        }

        fun getCameraMatrix(camera: Camera): Mat4 {
            val diff = camera.position - camera.target
            val cZ = diff / length(diff)

            val productUpZ = cross(camera.up, cZ)
            val cX = productUpZ / length(productUpZ)

            val productZX = cross(cZ, cX)
            val cY = productZX / length(productZX)

            return Mat4(
                    Float4(cX.x, cX.y, cX.z, dot(cX, camera.position)),
                    Float4(cY.x, cY.y, cY.z, dot(cY, camera.position)),
                    Float4(cZ.x, cZ.y, cZ.z, dot(cZ, camera.position)),
                    Float4(0f, 0f, 0f, 1f)
            )
        }

        fun getProjectionMatrix(theta: Float, sX: Float, sY: Float): Mat4 {
            //TODO: Mat[1, 1] has alpha in the original formula (here theta was used)
            return Mat4(
                    Float4((sX / 2f) * cot(theta / 2f), 0f, -sX / 2f, 0f),
                    Float4(0f, (-sX / 2f) * (cot(theta / 2f) / theta), -sY / 2f, 0f),
                    Float4(0f, 0f, 0f, -1f),
                    Float4(0f, 0f, -1f, 0f)
            )
        }

        private fun cot(tan: Float): Float {
            return 1f / tan
        }
    }

    fun draw(pixelWriter: PixelWriter, camera: Camera) {
        // pixelWriter.setColor(coordinate.x, coordinate.y, color)

    }

    protected lateinit var tuples: Array<Vertex>
    protected lateinit var position: Float3
    protected lateinit var rotation: Float3

}