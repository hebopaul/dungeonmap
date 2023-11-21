package com.example.dungeonmap

import android.util.Size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.Position
import com.example.dungeonmap.data.plus
import com.example.dungeonmap.data.times
import java.util.UUID

class MainViewModel: ViewModel() {

    data class Token(
        val id: UUID = UUID.randomUUID(),
        val position: Position,
        val drawable: Int
    ) {
        fun movedBy(offset: Size) =
            Token(id, position.plus(offset), drawable)
    }


    // These values could be used to immediately modify the
    // positions/sizes of any token.

    var worldOffset by mutableStateOf(Size(0, 0))
        private set

    var worldScale by mutableStateOf<Float>(1f)
        private set

    private var _playerToken by mutableStateOf(Token(
        position = Position(0 , 0),
        drawable = R.drawable.minotaur_berserker))

    val playerToken = snapshotFlow<Token?> {
        _playerToken.copy(
            position =
                (_playerToken.position * worldScale)
                    + worldOffset
        )
    }


    fun moveToken(tokenId: UUID, offset: Size) {
        // No need to check the token Id, because there is on one token at this moment.
        // But later, it should filter the tokens list.
        _playerToken = _playerToken.movedBy(offset)
    }

    fun updateWorldScale(scale: Float) {
        this.worldScale = scale
            .coerceIn(0.4f, 10f)
    }

    fun updateWorldOffset(offset: Size) {
        this.worldOffset = this.worldOffset.let {
            Size(
                /* width = */ it.width + offset.width,
                /* height = */ it.height + offset.height
            )
        }
    }


}