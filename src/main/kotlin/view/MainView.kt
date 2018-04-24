package view

import events.ImageCloseEvent
import events.ImageLoadedEvent
import events.ImageResetEvent
import events.ImageSaveEvent
import javafx.geometry.Orientation
import javafx.scene.control.TabPane
import javafx.scene.layout.VBox
import service.ImageService
import tornadofx.*
import viewmodel.ICanvasViewModel
import viewmodel.IFilterViewModel

class MainView : View() {

    private val filterViewModel: IFilterViewModel by di()
    private val canvasViewModel: ICanvasViewModel by di()
    private val imageService: ImageService by di()

    override val root = borderpane {
        prefHeight = 800.0
        prefWidth = 1000.0
        minWidth = 400.0
        minHeight = 400.0
        top {
            menubar {
                menu("File") {
                    item("Open image", "Ctrl+O") {
                        setOnAction {
                            imageService.loadImage()
                            fire(ImageLoadedEvent)
                        }
                    }
                    item("Save image", "Shortcut+S") {
                        setOnAction {
                            fire(ImageSaveEvent)
                        }
                    }
                    item("Reset image", "Ctrl+R") {
                        setOnAction {
                            fire(ImageResetEvent)
                        }
                    }
                    separator()
                    item("Close image", "Ctrl+Q") {
                        setOnAction {
                            fire(ImageCloseEvent)
                        }
                    }
                }
                menu("Dithering") {
                    item("Random").command = filterViewModel.ditheringRandomCommand
                    item("Average").command = filterViewModel.ditheringAverageCommand
                    item("Ordered").command = filterViewModel.ditheringOrderedCommand
                }
                menu("Quantization") {
                    item("Popularity").command = filterViewModel.quantizationPopularityCommand
                    item("Median cut").command = filterViewModel.quantizationMedianCutCommand
                }
            }
        }
        center {
            tabpane {
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                tab("Filters", VBox()) {
                    splitpane(Orientation.HORIZONTAL) {
                        scrollpane {
                            imageview {
                                imageProperty().bind(filterViewModel.leftImage)
                            }
                        }
                        scrollpane {
                            imageview {
                                imageProperty().bind(filterViewModel.rightImage)
                            }
                        }
                    }
                }
                tab("Canvas", VBox()) {
                    scrollpane {
                        imageview {
                            imageProperty().bind(canvasViewModel.imageProperty)
                            setOnMouseClicked {
                                canvasViewModel.onMouseClick(it)
                            }
                        }
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
                            combobox(filterViewModel.grayLevelProperty, filterViewModel.grayLevelsProperty) {
                                setOnAction {
                                    filterViewModel.commit()
                                }
                            }
                            label("Dimension of dither matrix")
                            combobox(filterViewModel.ditherMatrixDimensionProperty, filterViewModel.ditherMatrixDimensionsProperty) {
                                setOnAction {
                                    filterViewModel.commit()
                                }
                            }
                            label("Number of quantization colors")
                            combobox(filterViewModel.quantizationColorLevelProperty, filterViewModel.quantizationColorLevelsProperty) {
                                setOnAction {
                                    filterViewModel.commit()
                                }
                            }
                        }
                    }
                }
                item("Canvas parameters") {
                    squeezebox {
                        fold("Circle", expanded = true) {
                            label("Circle radius")
                            combobox(canvasViewModel.circleRadiusProperty, canvasViewModel.circleRadiusesProperty) {
                                setOnAction {
                                    canvasViewModel.commit()
                                }
                            }
                            label("Line thickness")
                            combobox(canvasViewModel.lineThicknessProperty, canvasViewModel.lineThicknessesProperty) {
                                setOnAction {
                                    canvasViewModel.commit()
                                }
                            }
                            label("Pen thickness")
                            combobox(canvasViewModel.penThicknessProperty, canvasViewModel.penThicknessesProperty) {
                                setOnAction {
                                    canvasViewModel.commit()
                                }
                            }
                            label("Drawing type")
                            combobox(canvasViewModel.drawingTypeProperty, canvasViewModel.drawingTypesProperty) {
                                setOnAction {
                                    canvasViewModel.commit()
                                }
                            }
                            label("Shape")
                            combobox(canvasViewModel.shapeProperty, canvasViewModel.shapesProperty) {
                                setOnAction {
                                    canvasViewModel.commit()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}