package model.canvas

abstract class ThickLineDecorator(protected val thickLineStrategy: ThickLineStrategy): ThickLineStrategy by thickLineStrategy