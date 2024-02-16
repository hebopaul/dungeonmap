package com.example.dungeonmap.data

import com.example.dungeonmap.Position
import java.util.UUID


sealed class VisibleEffect (
    open val position: Position,
    open val uuid: UUID = UUID.randomUUID()
)

data class Circle (
    override val position: Position =  Position(0f, 0f),
    val radius: Float = 1f,
): VisibleEffect(position)

data class Rectangle (
    override val position: Position =  Position(0f, 0f),
    val width: Float = 1f,
    val height: Float = 1f,
): VisibleEffect(position)

data class Polygon (
    override val position: Position =  Position(0f, 0f),
    val points: List<Position> = listOf(),
): VisibleEffect(position)

data class Line (
    override val position: Position =  Position(0f, 0f),
    val startPosition: Position,
    val endPosition: Position
): VisibleEffect(position)

data class PointerEffect (
    val position: Position
)