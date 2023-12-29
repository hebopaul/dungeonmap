package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset
import com.example.dungeonmap.R
import com.example.dungeonmap.defaultTokenSize
import java.util.UUID

data class Token (
    val tokenId: UUID = UUID.randomUUID(),
    var position: Offset = Offset(0F, 0F),
    var imageResource: Int = R.drawable.minotaur_berserker,
    var name: String? = null,
    var isEnemy: Boolean = false,
    var scale: Float = 1F,
    var tokenSize: Float = if (defaultTokenSize != null) defaultTokenSize!! else 1F,

)