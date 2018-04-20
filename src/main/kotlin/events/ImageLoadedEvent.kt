package events

import tornadofx.*

object ImageLoadedEvent : FXEvent(EventBus.RunOn.BackgroundThread)