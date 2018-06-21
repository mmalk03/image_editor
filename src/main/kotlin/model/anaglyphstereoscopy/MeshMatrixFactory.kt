package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class MeshMatrixFactory {

    //distance between the eyes in mm
    private val e = 50f
    //physical distance to the screen in px
    private val d = 100f

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

    fun getTranslationMatrix(v: Float3): Mat4 {
        return transpose(Mat4(
                Float4(1f, 0f, 0f, v.x),
                Float4(0f, 1f, 0f, v.y),
                Float4(0f, 0f, 0f, v.z),
                Float4(0f, 0f, 0f, 1f))
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
//        return transpose(Mat4(
//                Float4((sX / 2f) * cot(theta / 2f), 0f, -sX / 2f, 0f),
//                Float4(0f, (-sY / 2f) * cot(theta / 2f), -sY / 2f, 0f),
//                Float4(0f, 0f, 0f, -1f),
//                Float4(0f, 0f, -1f, 0f)
//        ))
        return transpose(Mat4(
                Float4(d, 0f, -sX / 2f, 0f),
                Float4(0f, d, -sY / 2f, 0f),
                Float4(0f, 0f, 0f, -1f),
                Float4(0f, 0f, -1f, 0f)
        ))
    }

    fun getStereoscopyLeftProjectionMatrix(cX: Float, cY: Float): Mat4 {
        return transpose(Mat4(
                Float4(d, 0f, -cX / 2f, -d * e / 2),
                Float4(0f, d, -cY / 2f, 0f),
                Float4(0f, 0f, 0f, -1f),
                Float4(0f, 0f, -1f, 0f)
        ))
    }

    fun getStereoscopyRightProjectionMatrix(cX: Float, cY: Float): Mat4 {
        return transpose(Mat4(
                Float4(d, 0f, -cX / 2f, d * e / 2),
                Float4(0f, d, -cY / 2f, 0f),
                Float4(0f, 0f, 0f, -1f),
                Float4(0f, 0f, -1f, 0f)
        ))
    }

    private fun cot(f: Float): Float {
        return 1f / tan(f)
    }
}