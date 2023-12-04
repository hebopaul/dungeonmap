package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset
import com.example.dungeonmap.R

data class MapState (
    var imageResource: Int = R.drawable.m03_tombofhorrors_300,
    var mapOffset: Offset = Offset(0F, 0F),
    var mapScale: Float = 1F,
    var isScaleLocked: Boolean = false
)