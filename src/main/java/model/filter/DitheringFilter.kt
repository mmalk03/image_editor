package main.java.model.filter

import javafx.beans.property.SimpleIntegerProperty
import tornadofx.Controller

class DitheringFilter : Controller() {

    val grayLevel = SimpleIntegerProperty(2)
}