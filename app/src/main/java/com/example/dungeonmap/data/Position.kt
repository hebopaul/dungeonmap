package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset

data class Position(
    val x: Float,
    val y: Float
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

operator fun Position.div(offset: Offset) = Position(
    x = this.x / offset.x,
    y = this.y / offset.y
)