package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.example.dungeonmap.Position
import com.example.dungeonmap.toOffset
import java.util.UUID


sealed class VisibleEffect (
    open val position: Position,
    open val color: Color = Color.Gray,
    open val alpha: Float = 0.6F,
    open val uuid: UUID = UUID.randomUUID()
) {
    abstract fun draw():  DrawScope.() -> Unit
}

data class Circle (
    override val position: Position =  Position(0f, 0f),
    val radius: Float = 1f
): VisibleEffect(position) {
    override fun draw():  DrawScope.() -> Unit = {
        drawCircle(
            color = color,
            center = Offset( position.x, position.y),
            radius = radius,
            alpha = alpha
        )
    }
}

data class Rectangle (
    override val position: Position =  Position(0f, 0f),
    val dimensions: Size,
): VisibleEffect(position) {
    override fun draw():  DrawScope.() -> Unit = {
        drawRect(
            color = color,
            topLeft = position.toOffset(),
            size = dimensions,
            alpha = alpha
        )
    }
}

data class Polygon (
    override val position: Position =  Position(0f, 0f),
    var points: List<Position> = listOf(),
): VisibleEffect(position) {
    override fun draw():  DrawScope.() -> Unit = {
            drawPath(
                path = Path().apply {
                    if (points.isNotEmpty()) moveTo(
                        points[0].x + position.x,
                        points[0].y + position.y
                    )
                    if (points.size > 1) points
                        .drop(1)
                        .forEach { point -> lineTo(
                            point.x + position.x,
                            point.y + position.y
                            )
                        }
                },
                color = color,
                style = Fill,
                alpha = alpha
            )
    }

    fun addPoint (point: Position) { points += point}
}

data class Line (
    override val position: Position =  Position(0f, 0f),
    val offset: Offset
): VisibleEffect(position) {
    override fun draw():  DrawScope.() -> Unit = {
        drawLine(
            color = color,
            start = position.toOffset(),
            end = position.toOffset() + offset,
            alpha = alpha,
            strokeWidth = 15F
        )
    }
}

data class PointerEffect (
    override val position: Position
) : VisibleEffect (position) {
    override fun draw():  DrawScope.() -> Unit = {
        repeat(20) { rep ->
            drawCircle(
                color = Color.Black,
                center = Offset( position.x, position.y),
                radius = if (rep == 1) 100F
                    else if (rep % 2 == 0) 20F else 5F,
                alpha = 0.25F
            )
        }
    }
}