package model.filling

import javafx.scene.paint.Color
import java.util.*

class RandomColorStrategy : ColorStrategy {

    private val random = Random()
    private var color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble())
    private val colorRedInc = random.nextDouble() / 0.2
    private val colorGreenInc = random.nextDouble() / 0.2
    private val colorBlueInc = random.nextDouble() / 0.2

    override fun getColor(x: Int, y: Int): Color {
        color = Color.color(clamp(color.red + colorRedInc), clamp(color.green + colorGreenInc), clamp(color.blue + colorBlueInc))
        return color
    }

    private fun clamp(d: Double): Double {
        return d % 1.0
    }
}