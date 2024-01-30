package com.example.dungeonmap.data

import android.graphics.Bitmap
import androidx.compose.ui.graphics.painter.Painter

data class StockImage(
    val id: Int,
    val name: String,
    val image: Painter
)
