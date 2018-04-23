package model.canvas

abstract class LineDecorator(protected val lineStrategy: LineStrategy): LineStrategy by lineStrategy