package module

import com.authzee.kotlinguice4.KotlinModule
import model.filter.grayscale.GrayscaleFilterStrategy
import model.filter.grayscale.ScientificGrayscaleFilterStrategy
import model.shape.CircleStrategy
import model.shape.LineStrategy
import model.shape.MidpointCircleStrategy
import model.shape.SymmetricBresenhamLineStrategy
import service.ImageFileChooserService
import service.ImageService

class MainModule : KotlinModule() {
    override fun configure() {
        bind<ImageService>().to<ImageFileChooserService>()
        bind<GrayscaleFilterStrategy>().to<ScientificGrayscaleFilterStrategy>()
        bind<LineStrategy>().to<SymmetricBresenhamLineStrategy>()
        bind<CircleStrategy>().to<MidpointCircleStrategy>()
    }
}