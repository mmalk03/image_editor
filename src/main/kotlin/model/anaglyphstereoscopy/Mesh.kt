package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float3
import com.curiouscreature.kotlin.math.Float4

abstract class Mesh {
    abstract val vertices: Array<Float4>
    var position = Float3(0f, 0f, 0f)
    var rotation = Float3(0f, 0f, 0f)
}