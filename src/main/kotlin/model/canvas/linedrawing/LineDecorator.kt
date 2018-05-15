package model.canvas.linedrawing

abstract class LineDecorator(protected val lineStrategy: LineStrategy): LineStrategy by lineStrategy