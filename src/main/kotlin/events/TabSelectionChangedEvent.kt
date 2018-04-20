package events

import tornadofx.*

object TabSelectionChangedEvent : FXEvent(EventBus.RunOn.BackgroundThread)