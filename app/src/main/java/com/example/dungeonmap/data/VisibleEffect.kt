package com.example.dungeonmap.data

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import com.example.dungeonmap.Position
import java.util.UUID


sealed class VisibleEffect (
    open val position: Position,
    open val color: Color = Color.Black,
    open val alpha: Float = 0.6F,
    open val uuid: UUID = UUID.randomUUID()
) {
    abstract val draw: @Composable DrawScope.() -> Unit
}

data class Circle (
    override val position: Position =  Position(0f, 0f),
    val radius: Float = 1f
): VisibleEffect(position) {
    override val draw: @Composable DrawScope.() -> Unit = {
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
    val width: Float = 1f,
    val height: Float = 1f,
): VisibleEffect(position) {
    override val draw: @Composable DrawScope.() -> Unit = {
        drawRect(
            color = color,
            topLeft = Offset( position.x, position.y),
            size = Size(width, height),
            alpha = alpha
        )
    }
}

data class Polygon (
    override val position: Position =  Position(0f, 0f),
    val points: List<Position> = listOf(),
): VisibleEffect(position) {
    override val draw: @Composable DrawScope.() -> Unit = {
        drawPath(
            path = Path().apply {
                if (points.isNotEmpty()) moveTo(points[0].x, points[0].y)
                if (points.size > 1) points
                    .drop(1)
                    .forEach { point -> lineTo(point.x, point.y) }
            },
            color = color,
            style = Fill,
            alpha = alpha
        )
    }
}

data class Line (
    override val position: Position =  Position(0f, 0f),
    val endPosition: Position
): VisibleEffect(position) {
    override val draw: @Composable DrawScope.() -> Unit = {
        drawLine(
            color = color,
            start = Offset( position.x, position.y),
            end = Offset( endPosition.x, endPosition.y),
            alpha = alpha,
            strokeWidth = 15F
        )
    }
}

data class PointerEffect (
    override val position: Position
) : VisibleEffect (position) {
    override val draw: @Composable DrawScope.() -> Unit = {
        repeat(20) { rep ->
            drawCircle(
                color = Color.Black,
                center = Offset( position.x, position.y),
                radius = animateFloatAsState(
                    targetValue =   if (rep == 1) 100F
                                    else if (rep % 2 == 0) 20F else 5F,
                    animationSpec = tween(300)
                ).value,
                alpha = 0.25F
            )
        }
    }
}