package model.anaglyphstereoscopy

import com.curiouscreature.kotlin.math.Float4
import javafx.scene.image.PixelWriter
import javafx.scene.paint.Color

interface LineDrawer {
    fun drawLine(v1: Float4, v2: Float4, pixelWriter: PixelWriter, imageWidth: Float, imageHeight: Float, color: Color)
}
