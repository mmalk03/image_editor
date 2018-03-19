package main.java.view

import javafx.geometry.Orientation
import main.java.viewmodel.MainViewModel
import tornadofx.*

class MainView : View() {

    val mainViewModel: MainViewModel by inject()

    override val root = borderpane {
        prefHeight = 800.0
        prefWidth = 1000.0
        minWidth = 400.0
        minHeight = 400.0
        top {
            menubar {
                menu("File") {
                    item("Open image", "Ctrl+O") {
                        setOnAction { mainViewModel.onOpenImage() }
                    }
                    item("Save image", "Shortcut+S") {
                        setOnAction { mainViewModel.onSaveImage() }
                    }
                    item("Reset image", "Ctrl+R") {
                        setOnAction { mainViewModel.onResetImage() }
                    }
                    separator()
                    item("Close image", "Ctrl+Q") {
                        setOnAction { mainViewModel.onCloseImage() }
                    }
                }
                menu("Linear filters") {
                    item("Inversion")
                    item("Grayscale")
                }
                menu("Dithering") {
                    menu("Random")
                    item("Average")
                    item("Ordered")
                }
                menu("Quantization") {
                    item("Popularity")
                    item("Median cut") {
                        //                        action {
//                            runAsync{
//                                controller.loadText()
//                            } ui { loadedText ->
//                                textField.text = loadedText
//                            }
//                        }
                    }
                }
            }
        }
        center {
            splitpane(Orientation.HORIZONTAL) {
                scrollpane {
                    imageview {
                        imageProperty().bind(mainViewModel.originalImage)
                    }
                }
                scrollpane {
                    imageview {
                        imageProperty().bind(mainViewModel.filteredImage)
                    }
                }
            }
        }
        left {
            drawer {
                item("Filter parameters") {
                    squeezebox {
                        fold("Dithering", expanded = true) {
                            vbox {}
                            label("Number of gray levels")
                            combobox(mainViewModel.grayLevel, mainViewModel.grayLevels) {
                                setOnAction {
                                    mainViewModel.commit()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}