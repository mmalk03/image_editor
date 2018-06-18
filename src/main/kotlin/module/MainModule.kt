package module

import com.authzee.kotlinguice4.KotlinModule
import com.google.inject.Singleton
import model.canvas.CoverageShapeDrawer
import model.canvas.ICoverageShapeDrawer
import model.canvas.IShapeDrawer
import model.canvas.ShapeDrawer
import model.canvas.linedrawing.*
import model.clipping.ClippingStrategy
import model.clipping.CoherSutherlandClippingStrategy
import model.filling.ActiveEdgeTableFillingStrategy
import model.filling.FillingStrategy
import model.filter.IImageFilter
import model.filter.ImageFilter
import model.filter.grayscale.GrayscaleFilterStrategy
import model.filter.grayscale.ScientificGrayscaleFilterStrategy
import model.flood.FloodFillingStrategy
import model.flood.ThresholdFloodFillingStrategy
import service.ImageFileChooserService
import service.ImageService
import viewmodel.*

class MainModule : KotlinModule() {
    override fun configure() {
        bind<ImageService>().to<ImageFileChooserService>().`in`<Singleton>()
        bind<GrayscaleFilterStrategy>().to<ScientificGrayscaleFilterStrategy>()
        bind<LineStrategy>().to<SymmetricBresenhamLineStrategy>()
        bind<OptimalLineStrategy>().to<SymmetricBresenhamOptimalLineStrategy>()
        bind<ClippingStrategy>().to<CoherSutherlandClippingStrategy>()
        bind<FillingStrategy>().to<ActiveEdgeTableFillingStrategy>()
        bind<CircleStrategy>().to<MidpointCircleStrategy>()
        bind<ThickLineStrategy>().to<GuptaSproullThickLineStrategy>()
        bind<ISuperSamplingStrategy>().to<SuperSamplingStrategy>()
        bind<IShapeDrawer>().to<ShapeDrawer>()
        bind<ICoverageShapeDrawer>().to<CoverageShapeDrawer>()
        bind<IImageFilter>().to<ImageFilter>()
        bind<IFilterViewModel>().to<FilterViewModel>()
        bind<ICanvasViewModel>().to<CanvasViewModel>()
        bind<IClippingViewModel>().to<ClippingViewModel>()
        bind<IFillingViewModel>().to<FillingViewModel>()
        bind<IFloodFillingViewModel>().to<FloodFillingViewModel>()
        bind<IAnaglyphStereoscopyViewModel>().to<AnaglyphStereoscopyViewModel>()
    }
}