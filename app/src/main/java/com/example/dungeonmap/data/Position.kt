package com.example.dungeonmap.data

import android.util.Size

data class Position(
    val x: Int,
    val y: Int
)

operator fun Position.plus(size: Size) = Position(
    x = this.x + size.width,
    y = this.y + size.height
)

operator fun Position.times(scale: Float) = Position(
    x = (this.x * scale).toInt(),
    y = (this.y * scale).toInt()
)