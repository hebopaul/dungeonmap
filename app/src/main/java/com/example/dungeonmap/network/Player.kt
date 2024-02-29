package com.example.dungeonmap.network

import androidx.compose.ui.graphics.Color

data class Player(
    val endpointId: String = "",
    val userName: String = "unknown",
    val color: Color = Color.Gray,
    var isHost: Boolean = false,
    val isConnected: Boolean = false
)
