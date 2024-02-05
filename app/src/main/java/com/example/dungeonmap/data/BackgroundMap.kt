package com.example.dungeonmap.data

import com.example.dungeonmap.R

data class BackgroundMap(
    val resId: Int? = R.drawable.map_tombofhorrors,
    val uri: String? = null,
    val isSelected: Boolean = true
)