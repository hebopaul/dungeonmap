package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset
import com.example.dungeonmap.R
import java.util.UUID

data class Token (
    private val tokenId: UUID = UUID.randomUUID(),
    var position: Offset = Offset(0F, 0F),
    var imageResource: Int = R.drawable.minotaur_berserker,
    var name: String? = null,
    var isEnemy: Boolean = false,
    var scale: Float = 1F

)