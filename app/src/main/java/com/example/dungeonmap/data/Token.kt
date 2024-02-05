package com.example.dungeonmap.data

import com.example.dungeonmap.R
import java.util.UUID

data class Token (

    var drawableRes: Int = R.drawable.minotaur_berserker,
    var name: String? = null,
    var isEnemy: Boolean = false,
    var isSelected: Boolean = false,
    var tokenSize: Float = 0.5F,
    var currentInitiave: Int = 0,
    var initiativeModifier: Int = 0,
    val uuid: UUID = UUID.randomUUID(),
    var position: Position = Position( 0F, 0F )
) {
    fun moveBy( position: Position) {
        this.position += position
    }
}


