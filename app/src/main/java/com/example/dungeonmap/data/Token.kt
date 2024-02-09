package com.example.dungeonmap.data

import com.example.dungeonmap.Position
import com.example.dungeonmap.R
import java.util.UUID

data class Token (

    val drawableRes: Int = R.drawable.minotaur_berserker,
    val name: String = "Unknown",
    val isEnemy: Boolean = false,
    val isSelected: Boolean = false,
    val size: Float = 1F,
    val currentInitiative: Int = 0,
    val initiativeModifier: Int = 0,
    val uuid: UUID = UUID.randomUUID(),
    var position: Position = Position( 0F, 0F )
)


