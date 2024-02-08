package com.example.dungeonmap

import androidx.compose.ui.geometry.Offset

class Position(
    val x: Float,
    val y: Float
)

fun Position.coerceAtMostScalable(width: Float, height: Float, scale: Float): Position =
    Position(
        x = if(this.x < 0) this.x.coerceAtLeast(-(width * scale))
            else this.x.coerceAtMost(width * scale),
        y = if(this.y < 0) this.x.coerceAtLeast(-(height * scale))
            else this.y.coerceAtMost(height * scale)
    )

operator fun Position.plus(offset: Offset) = Position(
    x = this.x + offset.x,
    y = this.y + offset.y
)

operator fun Position.plus(position: Position) = Position(
    x = this.x + position.x,
    y = this.y + position.y
)

operator fun Position.times(scale: Float) = Position(
    x = this.x * scale,
    y = this.y * scale
)

operator fun Position.minus(offset: Offset) = Position(
    x = this.x - offset.x,
    y = this.y - offset.y
)

operator fun Position.div(scale: Float) = Position(
    x = this.x / scale,
    y = this.y / scale
)