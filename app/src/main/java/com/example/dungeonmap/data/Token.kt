package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset
import com.example.dungeonmap.R
import java.util.UUID

data class Token (

    var drawableRes: Int = R.drawable.minotaur_berserker,
    var name: String? = null,
    var isEnemy: Boolean = false,
    var isSelected: Boolean = false,
    var scale: Float = 1F,
    var tokenSize: Float = 0.2F,
    var currentInitiave: Int = 0,
    var initiativeModifier: Int = 0,
    val uuid: UUID = UUID.randomUUID(),
    var position: Offset = Offset(0F, 0F)

)
