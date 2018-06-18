package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.*
import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

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

            return transpose(Mat4(
                    Float4(cX.x, cX.y, cX.z, dot(cX, camera.position)),
                    Float4(cY.x, cY.y, cY.z, dot(cY, camera.position)),
                    Float4(cZ.x, cZ.y, cZ.z, dot(cZ, camera.position)),
                    Float4(0f, 0f, 0f, 1f)
            ))
        }

        fun getProjectionMatrix(theta: Float, sX: Float, sY: Float): Mat4 {
            return transpose(Mat4(
                    Float4((sX / 2f) * cot(theta / 2f), 0f, -sX / 2f, 0f),
                    Float4(0f, (-sY / 2f) * cot(theta / 2f), -sY / 2f, 0f),
                    Float4(0f, 0f, 0f, -1f),
                    Float4(0f, 0f, -1f, 0f)
            ))
        }

        private fun cot(f: Float): Float {
            return 1f / tan(f)
        }
    }

    private val blackColor = Color.color(0.0, 0.0, 0.0)!!

    protected lateinit var tuples: Array<Float4>
    protected lateinit var position: Float3
    protected lateinit var rotation: Float3

    fun draw(pixelWriter: PixelWriter, imageWidth: Float, imageHeight: Float, camera: Camera) {
        val cameraMatrix = getCameraMatrix(camera)
        val projectionMatrix = getProjectionMatrix(90f, imageWidth, imageHeight)
//        val transformMatrix = cameraMatrix * projectionMatrix

        for (t in tuples) {
            val pointInCameraCoord = cameraMatrix * t
            val point = projectionMatrix * pointInCameraCoord
            point.transform { fl -> fl / point.w }
            if (point.x >= 0 && point.x < imageWidth && point.y >= 0 && point.y < imageHeight) {
                pixelWriter.setColor(point.x.toInt(), point.y.toInt(), blackColor)
            }
        }
//        for (t in tuples) {
//            val point = transformMatrix * t
//            point.transform { fl -> fl / point.w }
//            if (point.x >= 0 && point.x < imageWidth && point.y >= 0 && point.y < imageHeight) {
//                pixelWriter.setColor(point.x.toInt(), point.y.toInt(), blackColor)
//            }
//        }
    }
}