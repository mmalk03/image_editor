package viewmodel

import events.*
import tornadofx.*

abstract class MyViewModel : ViewModel(), ImageOperationListener {
    init {
        subscribe<ImageCloseEvent> { onCloseImage() }
        subscribe<ImageSaveEvent> { onSaveImage() }
        subscribe<ImageLoadedEvent> { onOpenImage() }
        subscribe<ImageResetEvent> { onResetImage() }
    }
}