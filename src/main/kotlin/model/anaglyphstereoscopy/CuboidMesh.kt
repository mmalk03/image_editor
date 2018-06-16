package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float4

class CuboidMesh(val width: Float, val height: Float, val depth: Float) : Mesh() {

    init {
        val frontNormalVector = Float4(0f, 0f, 1f, 0f)
        val rightNormalVector = Float4(1f, 0f, 1f, 0f)
        val backNormalVector = Float4(1f, 0f, 0f, 0f)
        val leftNormalVector = Float4(0f, 0f, 0f, 0f)
        val bottomNormalVector = Float4(0f, 0f, 0f, 0f)
        val topNormalVector = Float4(0f, 0f, 1f, 0f)
        tuples = arrayOf(
                //front face
                Vertex(Float4(0f, 0f, depth, 1f), frontNormalVector),
                Vertex(Float4(width, 0f, depth, 1f), frontNormalVector),
                Vertex(Float4(width, height, depth, 1f), frontNormalVector),
                Vertex(Float4(0f, height, depth, 1f), frontNormalVector),
                //right face
                Vertex(Float4(width, 0f, depth, 1f), rightNormalVector),
                Vertex(Float4(width, 0f, 0f, 1f), rightNormalVector),
                Vertex(Float4(width, height, 0f, 1f), rightNormalVector),
                Vertex(Float4(width, height, depth, 1f), rightNormalVector),
                //back face
                Vertex(Float4(width, 0f, 0f, 1f), backNormalVector),
                Vertex(Float4(0f, 0f, 0f, 1f), backNormalVector),
                Vertex(Float4(0f, height, 0f, 1f), backNormalVector),
                Vertex(Float4(width, height, 0f, 1f), backNormalVector),
                //left face
                Vertex(Float4(0f, 0f, 0f, 1f), leftNormalVector),
                Vertex(Float4(0f, 0f, depth, 1f), leftNormalVector),
                Vertex(Float4(0f, height, depth, 1f), leftNormalVector),
                Vertex(Float4(0f, height, 0f, 1f), leftNormalVector),
                //bottom face
                Vertex(Float4(0f, 0f, depth, 1f), bottomNormalVector),
                Vertex(Float4(width, 0f, depth, 1f), bottomNormalVector),
                Vertex(Float4(width, 0f, 0f, 1f), bottomNormalVector),
                Vertex(Float4(0f, 0f, 0f, 1f), bottomNormalVector),
                //top face
                Vertex(Float4(0f, height, depth, 1f), topNormalVector),
                Vertex(Float4(width, height, depth, 1f), topNormalVector),
                Vertex(Float4(width, height, 0f, 1f), topNormalVector),
                Vertex(Float4(0f, height, 0f, 1f), topNormalVector)
        )
    }
}