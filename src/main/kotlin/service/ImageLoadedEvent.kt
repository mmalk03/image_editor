package service

import tornadofx.*

object ImageLoadedEvent : FXEvent(EventBus.RunOn.BackgroundThread)