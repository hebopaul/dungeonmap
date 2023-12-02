package com.example.dungeonmap.data

import androidx.compose.ui.geometry.Offset
import com.example.dungeonmap.R
import java.util.UUID

class TokenState (
    private val tokenId: UUID = UUID.randomUUID(),
    var position: Offset = Offset(
        0F + collectivePosition.x,
        0F + collectivePosition.y
    ),
    var imageResource: Int = R.drawable.minotaur_berserker,
    var name: String? = null,
    var isEnemy: Boolean = false

) {
    companion object {

        var collectivePosition: Offset = Offset(0f, 0f)
    }

    fun moveBy(offset: Offset) {
        position = Offset(
            position.x + offset.x,
            position.y + offset.y
        )
    }

    fun setImage(drawable: Int) {
        imageResource = drawable
    }

    fun setTokenName(newName: String) {
        name = newName
    }

    fun moveCollectivePosition (offset: Offset) {
        collectivePosition = Offset(
                (collectivePosition.x + offset.x).toFloat(),
                (collectivePosition.y + offset.y).toFloat()
        )
    }

}