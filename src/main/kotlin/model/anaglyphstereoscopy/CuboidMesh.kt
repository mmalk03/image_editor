package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float4

class CuboidMesh(width: Float, height: Float, depth: Float, initX: Float, initY: Float) : Mesh() {
    override var vertices = arrayOf(
            //front face
            Float4(-width / 2, -height / 2, depth / 2, 1f),
            Float4(width / 2, -height / 2, depth / 2, 1f),
            Float4(width / 2, height / 2, depth / 2, 1f),
            Float4(-width / 2, height / 2, depth / 2, 1f),
            //right face
            Float4(width / 2, -height / 2, depth / 2, 1f),
            Float4(width / 2, -height / 2, -depth / 2, 1f),
            Float4(width / 2, height / 2, -depth / 2, 1f),
            Float4(width / 2, height / 2, depth / 2, 1f),
            //back face
            Float4(width / 2, -height / 2, -depth / 2, 1f),
            Float4(-width / 2, -height / 2, -depth / 2, 1f),
            Float4(-width / 2, height / 2, -depth / 2, 1f),
            Float4(width / 2, height / 2, -depth / 2, 1f),
            //left face
            Float4(-width / 2, -height / 2, -depth / 2, 1f),
            Float4(-width / 2, -height / 2, depth / 2, 1f),
            Float4(-width / 2, height / 2, depth / 2, 1f),
            Float4(-width / 2, height / 2, -depth / 2, 1f),
            //bottom face
            Float4(width / 2, -height / 2, -depth / 2, 1f),
            Float4(width / 2, -height / 2, depth / 2, 1f),
            Float4(-width / 2, -height / 2, depth / 2, 1f),
            Float4(-width / 2, -height / 2, -depth / 2, 1f),
            //top face
            Float4(-width / 2, height / 2, -depth / 2, 1f),
            Float4(-width / 2, height / 2, depth / 2, 1f),
            Float4(width / 2, height / 2, depth / 2, 1f),
            Float4(width / 2, height / 2, -depth / 2, 1f))

    init {
//        position = Float3(initX, initY, 0f)
    }
}