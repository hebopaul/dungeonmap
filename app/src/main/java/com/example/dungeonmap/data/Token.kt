package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset
import com.example.dungeonmap.R
import java.util.UUID

data class Token(
    val resId: Int? = R.drawable.token_minotaur_berserker,
    val tokenUri: String? = null,
    val uuid: UUID = UUID.randomUUID(),
    val name: String? = null,
    var position: Offset = Offset( 0F, 0F),
    var size: Float = 1F,
    val initiativeMod: Int = 0,
    val currentInitiative: Int = 0,
    val isSelected: Boolean = false,
    val ownerPlayerId: UUID? = null

)
