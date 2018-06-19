package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float4
import com.curiouscreature.kotlin.math.Mat4
import com.google.inject.Inject
import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color
import model.anaglyphstereoscopy.mesh.Mesh
import model.canvas.linedrawing.OptimalLineStrategy

class MeshDrawingService @Inject constructor(private val meshMatrixFactory: MeshMatrixFactory,
                                             private val optimalLineStrategy: OptimalLineStrategy) : LineDrawer {

    private val blackColor = Color.color(0.0, 0.0, 0.0)
    private val redColor = Color.color(1.0, 0.0, 0.0)
    private val cyanColor = Color.color(0.0, 1.0, 1.0)

    fun draw(mesh: Mesh, pixelWriter: PixelWriter, imageWidth: Float, imageHeight: Float, camera: Camera) {
        val transformMatrix = getTransformMatrix(mesh)

        val mappingMatrix = getMappingMatrix(camera, imageWidth, imageHeight)
        mesh.draw(mappingMatrix, transformMatrix, pixelWriter, this, imageWidth, imageHeight, blackColor)

        val stereoscopyLeftMappingMatrix = getStereoscopyLeftMappingMatrix(camera, imageWidth, imageHeight)
        mesh.draw(stereoscopyLeftMappingMatrix, transformMatrix, pixelWriter, this, imageWidth, imageHeight, cyanColor)

        val stereoscopyRightMappingMatrix = getStereoscopyRightMappingMatrix(camera, imageWidth, imageHeight)
        mesh.draw(stereoscopyRightMappingMatrix, transformMatrix, pixelWriter, this, imageWidth, imageHeight, redColor)
    }

    private fun getMappingMatrix(camera: Camera, imageWidth: Float, imageHeight: Float): Mat4 {
        return meshMatrixFactory.getProjectionMatrix(90f, imageWidth, imageHeight) *
                meshMatrixFactory.getCameraMatrix(camera)
    }

    private fun getStereoscopyLeftMappingMatrix(camera: Camera, imageWidth: Float, imageHeight: Float): Mat4 {
        return meshMatrixFactory.getStereoscopyLeftProjectionMatrix(imageWidth, imageHeight) *
                meshMatrixFactory.getCameraMatrix(camera)
    }

    private fun getStereoscopyRightMappingMatrix(camera: Camera, imageWidth: Float, imageHeight: Float): Mat4 {
        return meshMatrixFactory.getStereoscopyRightProjectionMatrix(imageWidth, imageHeight) *
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