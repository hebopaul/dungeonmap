package com.example.dungeonmap.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Modifier.fadingEdges(
    edgeWidth: Float = 150F
): Modifier = this.then(
    Modifier
        // adding layer fixes issue with blending gradient and content
        .graphicsLayer { alpha = 0.99F }
        .drawWithContent {
            drawContent()

            val startColors = listOf(Color.Transparent, Color.Black)
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = startColors,
                    startX = 0F,
                    endX = edgeWidth
                ),
                blendMode = BlendMode.DstIn
            )

            val endColors = listOf(Color.Black, Color.Transparent)
            val endX = size.width
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = endColors,
                    startX = endX - edgeWidth,
                    endX = endX
                ),
                blendMode = BlendMode.DstIn
            )
        }
)