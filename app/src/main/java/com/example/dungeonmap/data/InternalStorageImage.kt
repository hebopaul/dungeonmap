package com.example.dungeonmap.data

import androidx.compose.ui.graphics.painter.Painter

data class InternalStorageImage(
    val name: String,
    val painter: Painter,
    val uri: String
)