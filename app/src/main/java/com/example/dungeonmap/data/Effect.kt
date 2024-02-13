package com.example.dungeonmap.data

import android.util.Size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.example.dungeonmap.Position


data class VisibleEffect(
    val shape: EffectShape = EffectShape.Circle,
    val color: EffectColor = EffectColor.Gray,
    val size: Size = Size(10, 10),
    val alpha: Float = 0.5F,
    val rotation: Float = 0F,
    val position: Position = Position(0F, 0F)
)

enum class EffectShape(val shape: Shape){
    Circle(CircleShape),
    Square(RectangleShape),
    Cone(TriangleShape),
}

enum class EffectColor (val color: Color) {
    White(Color.White),
    Black(Color.Black),
    Red(Color.Red),
    Green(Color.Green),
    Blue(Color.Blue),
    Yellow(Color.Yellow),
    Magenta(Color.Magenta),
    Cyan(Color.Cyan),
    Gray(Color.Gray)
}

private val TriangleShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
}
