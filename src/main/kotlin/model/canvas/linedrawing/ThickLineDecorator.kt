package model.canvas.linedrawing

abstract class ThickLineDecorator(protected val thickLineStrategy: ThickLineStrategy): ThickLineStrategy by thickLineStrategy