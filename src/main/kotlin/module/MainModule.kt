package module

import com.authzee.kotlinguice4.KotlinModule
import com.google.inject.Singleton
import model.canvas.*
import model.filter.IImageFilter
import model.filter.ImageFilter
import model.filter.grayscale.GrayscaleFilterStrategy
import model.filter.grayscale.ScientificGrayscaleFilterStrategy
import service.ImageFileChooserService
import service.ImageService
import viewmodel.CanvasViewModel
import viewmodel.FilterViewModel
import viewmodel.ICanvasViewModel
import viewmodel.IFilterViewModel

class MainModule : KotlinModule() {
    override fun configure() {
        bind<ImageService>().to<ImageFileChooserService>().`in`<Singleton>()
        bind<GrayscaleFilterStrategy>().to<ScientificGrayscaleFilterStrategy>()
        bind<LineStrategy>().to<SymmetricBresenhamLineStrategy>()
        bind<CircleStrategy>().to<MidpointCircleStrategy>()
        bind<ThickLineStrategy>().to<GuptaSproullThickLineStrategy>()
        bind<ISuperSamplingStrategy>().to<SuperSamplingStrategy>()
        bind<IShapeDrawer>().to<ShapeDrawer>()
        bind<ICoverageShapeDrawer>().to<CoverageShapeDrawer>()
        bind<IImageFilter>().to<ImageFilter>()
        bind<IFilterViewModel>().to<FilterViewModel>()
        bind<ICanvasViewModel>().to<CanvasViewModel>()
    }
}