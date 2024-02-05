package com.example.dungeonmap

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.Token
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.utilities.getDrawableResourcesIds
import com.example.dungeonmap.utilities.getStockImageList
import java.util.UUID
import kotlin.random.Random

const val MIN_SCALE: Float = 1F
const val MAX_SCALE: Float = 10F



class MainViewModel(val fileHandler: FileHandler) : ViewModel() {

    val stockMapsList = getStockImageList("map")
    val stockTokensList = getStockImageList("token")
    val stockD20List = getDrawableResourcesIds("d20")

    val randomD20 = stockD20List[Random.nextInt(stockD20List.size-1)]

    var activeMap by mutableStateOf(BackgroundMap())
        private set
    var activeTokens: MutableState<List<Token>> = mutableStateOf(mutableListOf())
        private set




    fun updateTokenPosition (uuid: UUID, newPosition: Offset) {
        activeTokens.value.forEachIndexed { index, token ->
            if (token.uuid == uuid) {
                activeTokens.value[index].position += newPosition
                return@forEachIndexed
            }
        }
    }

    fun updateTokenSize (uuid: UUID, multiplier: Float) {
        activeTokens.value.forEachIndexed { index, token ->
            if (token.uuid == uuid) {
                activeTokens.value[index].size *= multiplier
                return@forEachIndexed
            }
        }
    }

    fun addToken (resId: Int) {
        if(activeTokens.value.isNotEmpty())
            activeTokens.value += mutableListOf(
                Token(
                    resId = resId,
                    position = activeTokens.value.takeLast(1)[0].position + Offset( 10F, 10F),
                    size = activeTokens.value.takeLast(1)[0].size
                )
            )
        else activeTokens.value += mutableListOf( Token(resId = resId))
    }
}
