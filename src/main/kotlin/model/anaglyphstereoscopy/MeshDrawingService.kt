package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float4
import com.curiouscreature.kotlin.math.Mat4
import com.google.inject.Inject
import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color
import model.canvas.linedrawing.OptimalLineStrategy

class MeshDrawingService @Inject constructor(private val meshMatrixFactory: MeshMatrixFactory,
                                             private val optimalLineStrategy: OptimalLineStrategy) : LineDrawer {

    fun draw(mesh: Mesh, pixelWriter: PixelWriter, imageWidth: Float, imageHeight: Float, camera: Camera) {
        val mappingMatrix = getMappingMatrix(camera, imageWidth, imageHeight)
        val transformMatrix = getTransformMatrix(mesh)
        mesh.draw(mappingMatrix, transformMatrix, pixelWriter, this, imageWidth, imageHeight)
    }

    private fun getMappingMatrix(camera: Camera, imageWidth: Float, imageHeight: Float): Mat4 {
        return meshMatrixFactory.getProjectionMatrix(90f, imageWidth, imageHeight) *
                meshMatrixFactory.getCameraMatrix(camera)
    }

    private fun getTransformMatrix(mesh: Mesh): Mat4 {
        return meshMatrixFactory.getRotationMatrixX(mesh.rotation.x) *
                meshMatrixFactory.getRotationMatrixY(mesh.rotation.y) *
                meshMatrixFactory.getRotationMatrixZ(mesh.rotation.z) *
                meshMatrixFactory.getTranslationMatrix(mesh.position)
    }

    override fun drawLine(v1: Float4, v2: Float4, pixelWriter: PixelWriter, imageWidth: Float, imageHeight: Float, color: Color) {
        optimalLineStrategy.drawLine(v1.x.toInt(), v1.y.toInt(), v2.x.toInt(), v2.y.toInt(), imageWidth.toInt(), imageHeight.toInt(), pixelWriter, color)
    }
}