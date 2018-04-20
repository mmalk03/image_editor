package events

interface ImageOperationListener {
    fun onOpenImage()
    fun onSaveImage()
    fun onResetImage()
    fun onCloseImage()
}