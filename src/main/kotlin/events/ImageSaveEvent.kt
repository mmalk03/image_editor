package events

import tornadofx.*

object ImageSaveEvent : FXEvent(EventBus.RunOn.BackgroundThread)