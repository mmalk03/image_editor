package view

import javafx.geometry.Orientation
import viewmodel.MainViewModel
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
                menu("Dithering") {
                    item("Random").command = mainViewModel.ditheringRandomCommand
                    item("Average").command = mainViewModel.ditheringAverageCommand
                    item("Ordered").command = mainViewModel.ditheringOrderedCommand
                }
                menu("Quantization") {
                    item("Popularity").command = mainViewModel.quantizationPopularityCommand
                    item("Median cut").command = mainViewModel.quantizationMedianCutCommand
                }
            }
        }
        center {
            splitpane(Orientation.HORIZONTAL) {
                scrollpane {
                    imageview {
                        imageProperty().bind(mainViewModel.leftImage)
                    }
                }
                scrollpane {
                    imageview {
                        imageProperty().bind(mainViewModel.rightImage)
                    }
                }
            }
        }
        left {
            drawer {
                item("Filter parameters") {
                    squeezebox {
                        fold("Dithering", expanded = true) {
                            label("Number of gray levels")
                            combobox(mainViewModel.grayLevel, mainViewModel.grayLevels) {
                                setOnAction {
                                    mainViewModel.commit()
                                }
                            }
                            label("Dimension of dither matrix")
                            combobox(mainViewModel.ditherMatrixDimension, mainViewModel.ditherMatrixDimensions) {
                                setOnAction {
                                    mainViewModel.commit()
                                }
                            }
                            label("Number of quantization colors")
                            combobox(mainViewModel.quantizationColorLevel, mainViewModel.quantizationColorLevels) {
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