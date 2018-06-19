package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Float4

class CuboidMesh(dimensions: Float3, initPosition: Float3) : Mesh() {
    override var vertices = arrayOf(
            //front face
            Float4(-dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
            //right face
            Float4(dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
            //back face
            Float4(dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
            //left face
            Float4(-dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
            //bottom face
            Float4(dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, -dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, -dimensions.y / 2, -dimensions.z / 2, 1f),
            //top face
            Float4(-dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f),
            Float4(-dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, dimensions.y / 2, dimensions.z / 2, 1f),
            Float4(dimensions.x / 2, dimensions.y / 2, -dimensions.z / 2, 1f))

    init {
        position = Float3(initPosition.x, initPosition.y, initPosition.z)
    }
}