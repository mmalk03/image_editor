package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.*
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

        fun getCameraMatrix(cPos: Float3, cTarget: Float3, cUp: Float3): Mat4 {
            val diff = cPos - cTarget
            val cZ = diff / length(diff)

            val productUpZ = cross(cUp, cZ)
            val cX = productUpZ / length(productUpZ)

            val productZX = cross(cZ, cX)
            val cY = productZX / length(productZX)

            return Mat4(
                    Float4(cX.x, cX.y, cX.z, dot(cX, cPos)),
                    Float4(cY.x, cY.y, cY.z, dot(cY, cPos)),
                    Float4(cZ.x, cZ.y, cZ.z, dot(cZ, cPos)),
                    Float4(0f, 0f, 0f, 1f)
            )
        }
    }

    protected lateinit var vertices: Array<Float4>
    protected lateinit var position: Float3
    protected lateinit var rotation: Float3

}