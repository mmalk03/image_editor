package model.filling

import com.google.inject.Inject
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import model.canvas.Coordinate
import model.canvas.IShapeDrawer
import model.canvas.linedrawing.LineStrategy
import service.BigBlankImageService
import tornadofx.*
import java.util.*
import kotlin.math.abs

class FillingModel @Inject constructor(private val lineStrategy: LineStrategy,
                                       private val fillingStrategy: FillingStrategy,
                                       private val shapeDrawer: IShapeDrawer) {
    companion object {
        const val START_DISTANCE = 20
    }

    private var originalImage: Image? = null
    private val bigBlankImageService = BigBlankImageService()

    val imageProperty = SimpleObjectProperty<Image>()
    private var image by imageProperty

    val isPatternSelectedProperty = SimpleBooleanProperty(false)
    private var isPatternSelected by isPatternSelectedProperty

    private var polygon: Polygon? = null

    private var startedDrawingPolygon = false
    private var startCoordinate: Coordinate? = null
    private var prevCoordinate: Coordinate? = null

    private var randomColorStrategy = RandomColorStrategy()
    private var patternColorStrategy = PatternColorStrategy(Coordinate(0, 0))

    init {
        bigBlankImageService.loadImage()
    }

    fun addCoordinate(coordinate: Coordinate) {
        if (startedDrawingPolygon) {
            if (abs(startCoordinate!!.x - coordinate.x) < START_DISTANCE && abs(startCoordinate!!.y - coordinate.y) < START_DISTANCE) {
                drawLine(prevCoordinate!!, startCoordinate!!)
                fill()
                startedDrawingPolygon = false
            } else {
                polygon!!.add(coordinate)
                drawLine(prevCoordinate!!, coordinate)
                prevCoordinate = coordinate
            }
        } else {
            startCoordinate = coordinate
            prevCoordinate = coordinate
            polygon = Polygon(LinkedList())
            polygon!!.add(coordinate)
            startedDrawingPolygon = true
        }
    }

    private fun fill() {
        image = if (isPatternSelected) {
            fillingStrategy.fillPolygon(polygon!!, image, patternColorStrategy)
        } else {
            fillingStrategy.fillPolygon(polygon!!, image, randomColorStrategy)
        }
    }

    private fun drawLine(source: Coordinate, dest: Coordinate) {
        if (image == null) return
        val coordinates = lineStrategy.getCoordinates(source, dest)
        image = shapeDrawer.draw(image, coordinates)
    }

    fun onOpenImage(openedImage: Image) {
        image = openedImage
        originalImage = openedImage
    }

    fun onResetImage() {
        image = originalImage
    }

    fun onCloseImage() {
        image = null
        originalImage = null
    }
}