package main.java

import main.java.view.MainView
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus

class MyApplication : App(MainView::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}