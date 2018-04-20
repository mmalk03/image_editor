import com.google.inject.Guice
import module.MainModule
import tornadofx.*
import view.MainView
import kotlin.reflect.KClass

class MyApplication : App(MainView::class) {

    val injector = Guice.createInjector(MainModule())!!

    init {
        reloadStylesheetsOnFocus()
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>) = injector.getInstance(type.java)
        }
    }
}