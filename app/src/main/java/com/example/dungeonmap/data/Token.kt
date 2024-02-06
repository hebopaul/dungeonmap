package com.example.dungeonmap.data

import com.example.dungeonmap.R
import java.util.UUID

data class Token (

    val drawableRes: Int = R.drawable.minotaur_berserker,
    val name: String? = null,
    val isEnemy: Boolean = false,
    val isSelected: Boolean = false,
    val tokenSize: Float = 0.5F,
    val currentInitiave: Int = 0,
    val initiativeModifier: Int = 0,
    val uuid: UUID = UUID.randomUUID(),
    var position: Position = Position( 0F, 0F )
)


