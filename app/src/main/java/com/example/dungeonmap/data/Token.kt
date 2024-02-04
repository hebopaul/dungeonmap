package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset
import java.util.UUID

data class Token(
    val resId: Int,
    val uuid: UUID = UUID.randomUUID(),
    val name: String? = null,
    val positionOnMap: Offset,
    val size: Float,
    val initiativeMod: Int,
    val currentInitiative: Int = 0,
    val isSelected: Boolean = false,
    val ownerPlayerId: UUID? = null

)
