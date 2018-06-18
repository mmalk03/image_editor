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
import viewmodel.*

class MainView : View() {

    private val filterViewModel: IFilterViewModel by di()
    private val canvasViewModel: ICanvasViewModel by di()
    private val clippingViewModel: IClippingViewModel by di()
    private val fillingViewModel: IFillingViewModel by di()
    private val floodFillingViewModel: IFloodFillingViewModel by di()
    private val anaglyphStereoscopyViewModel: IAnaglyphStereoscopyViewModel by di()
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
                tab("Shape drawing", VBox()) {
                    scrollpane {
                        imageview {
                            imageProperty().bind(canvasViewModel.imageProperty)
                            setOnMouseClicked {
                                canvasViewModel.onMouseClick(it)
                            }
                        }
                    }
                }
                tab("Clipping", VBox()) {
                    scrollpane {
                        imageview {
                            imageProperty().bind(clippingViewModel.imageProperty)
                            setOnMouseClicked {
                                clippingViewModel.onMouseClick(it)
                            }
                        }
                    }
                }
                tab("Filling", VBox()) {
                    scrollpane {
                        imageview {
                            imageProperty().bind(fillingViewModel.imageProperty)
                            setOnMouseClicked {
                                fillingViewModel.onMouseClick(it)
                            }
                        }
                    }
                }
                tab("Flood filling", VBox()) {
                    scrollpane {
                        imageview {
                            imageProperty().bind(floodFillingViewModel.imageProperty)
                            setOnMouseClicked {
                                floodFillingViewModel.onMouseClick(it)
                            }
                        }
                    }
                }
                tab("Anaglyph stereoscopy", VBox()) {
                    scrollpane {
                        imageview {
                            imageProperty().bind(anaglyphStereoscopyViewModel.imageProperty)
                            setOnMouseClicked {
                                anaglyphStereoscopyViewModel.onMouseClick(it)
                            }
                        }
                    }
                }
            }
        }
        left {
            drawer {
                item("Filter") {
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
                item("Shape drawing") {
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
                item("Clipping") {
                    squeezebox {
                        fold("Circle", expanded = true) {
                            checkbox("Rectangle", clippingViewModel.isRectangleSelectedProperty) {
                                setOnAction {
                                    clippingViewModel.commit()
                                }
                            }
                        }
                    }
                }
                item("Filling") {
                    squeezebox {
                        checkbox("Pattern", fillingViewModel.isPatternSelectedProperty) {
                            setOnAction {
                                fillingViewModel.commit()
                            }
                        }
                    }
                }
                item("Flood filling") {
                    squeezebox {
                        fold("Threshold", expanded = true) {
                            combobox(floodFillingViewModel.thresholdProperty, floodFillingViewModel.thresholdsProperty) {
                                setOnAction {
                                    floodFillingViewModel.commit()
                                }
                            }
                        }
                    }
                }
                item("Anaglyph stereoscopy", expanded = true) {
                    vbox {
                        squeezebox {
                            fold("Shape", expanded = true) {
                                label("Shape")
                                combobox(anaglyphStereoscopyViewModel.shapeProperty, anaglyphStereoscopyViewModel.shapesProperty) {
                                    setOnAction {
                                        anaglyphStereoscopyViewModel.commit()
                                    }
                                }
                                label("Mesh density")
                                combobox(anaglyphStereoscopyViewModel.meshDensityProperty, anaglyphStereoscopyViewModel.meshDensitiesProperty) {
                                    setOnAction {
                                        anaglyphStereoscopyViewModel.commit()
                                    }
                                }
                            }
                        }
                        squeezebox {
                            fold("Cone/cylinder", expanded = false) {
                                label("Base radius")
                                combobox(anaglyphStereoscopyViewModel.cRadiusProperty, anaglyphStereoscopyViewModel.cRadiusesProperty) {
                                    setOnAction {
                                        anaglyphStereoscopyViewModel.commit()
                                    }
                                }
                                label("Height")
                                combobox(anaglyphStereoscopyViewModel.cHeightProperty, anaglyphStereoscopyViewModel.cHeightsProperty) {
                                    setOnAction {
                                        anaglyphStereoscopyViewModel.commit()
                                    }
                                }
                            }
                        }
                        squeezebox {
                            fold("Sphere", expanded = false) {
                                label("Radius")
                                combobox(anaglyphStereoscopyViewModel.sphereRadiusProperty, anaglyphStereoscopyViewModel.sphereRadiusesProperty) {
                                    setOnAction {
                                        anaglyphStereoscopyViewModel.commit()
                                    }
                                }
                            }
                        }
                        squeezebox {
                            fold("Cuboid", expanded = true) {
                                label("Edge length")
                                combobox(anaglyphStereoscopyViewModel.cuboidEdgeLengthProperty, anaglyphStereoscopyViewModel.cuboidEdgeLengthsProperty) {
                                    setOnAction {
                                        anaglyphStereoscopyViewModel.commit()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}