package com.example.dungeonmap.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.dungeonmap.R
import java.util.UUID

class TokenState (
    private val tokenId: UUID = UUID.randomUUID(),
    var position: MutableState<Offset> = mutableStateOf(Offset(
        0F + collectivePosition.value.x,
        0F + collectivePosition.value.y
    )),
    var imageResource: Int = R.drawable.minotaur_berserker,
    var name: String? = null,
    var isEnemy: Boolean = false

) {
    companion object {

        var collectivePosition: MutableState<Offset> = mutableStateOf(Offset(0f, 0f))


    }

    fun moveBy(offset: Offset) {
        position = mutableStateOf(Offset(
            position.value.x + offset.x,
            position.value.y + offset.y
        ))
    }

    fun setImage(drawable: Int?) {
        imageResource = drawable!!
    }

    fun setTokenName(newName: String) {
        name = newName
    }

    operator fun MutableState<Offset>.plus(offset: MutableState<Offset>) {


    }

    fun moveCollectivePosition (offset: Offset) {
        collectivePosition = mutableStateOf(
            Offset(
                (collectivePosition.value.x + offset.x).toFloat(),
                (collectivePosition.value.y + offset.y).toFloat()
            )
        )

    }

}