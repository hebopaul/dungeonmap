package com.example.dungeonmap

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.Position
import com.example.dungeonmap.data.StockImage
import com.example.dungeonmap.data.Token
import com.example.dungeonmap.data.plus
import com.example.dungeonmap.data.times
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.utilities.getDrawableResourcesIds
import com.example.dungeonmap.utilities.getStockImageList
import java.util.UUID
import kotlin.random.Random

const val MIN_SCALE: Float = 1F
const val MAX_SCALE: Float = 10F
const val X_BOUNDARY: Float = 350F
const val Y_BOUNDARY: Float = 500F

class MainViewModel(val fileHandler: FileHandler) : ViewModel() {

    //Loading all of the image resources we are going to need
    private val stockD20List: List<Int> = getDrawableResourcesIds("d20")
    val stockMapsList: List<StockImage> = getStockImageList("map")
    val stockTokensList: List<StockImage> = getStockImageList("token")


    //This is where we keep our global variables
    var globalPosition by mutableStateOf(Position(0F, 0F))
        private set

    var globalScale by mutableFloatStateOf(1F)
    

    var isPickerVisible by mutableStateOf(false)
    var userAddedMapsList by mutableStateOf(fileHandler.getInternalStorageMapList())
        private set
    var userAddedTokensList by mutableStateOf(fileHandler.getInternalStorageTokenList())
        private set
    var backgroundMap by mutableStateOf(BackgroundMap())
        private set

    private var _activeTokenList by mutableStateOf(listOf(Token()))


    val activeTokenList = snapshotFlow{
        _activeTokenList.map { token -> token.copy(
                position = globalPosition + (token.position * globalScale),
            )
        }
    }

    val randomD20 = stockD20List[Random.nextInt(stockD20List.size-1)]


    fun updateMapPosition(newOffset: Offset) {
        globalPosition += newOffset
    }

    //This function is called when the user pinches to zoom in or out
    fun updateGlobalScale(scaleChange: Float) {
        globalScale = (globalScale * scaleChange).coerceIn(MIN_SCALE, MAX_SCALE)
        globalPosition = (globalPosition * scaleChange)/*.coerceAtMostScalable(
                                                            X_BOUNDARY, Y_BOUNDARY, globalScale
                                                        )*/
        Log.d("Moved token","globalScale:${globalScale} "+"global: $globalPosition "+"token: ${_activeTokenList[0].position}")
    }

    //This function is called when the user drags the token
    fun updateTokenPosition(newPosition: Offset, uuid: UUID) {
        Log.d("Moved token","globalScale:${globalScale} "+"global: $globalPosition "+"token: ${_activeTokenList[0].position}")
        _activeTokenList = _activeTokenList.map { token ->
            if (token.uuid == uuid) token.copy(
                    position = token.position + newPosition / globalScale
                )
            else token
        }
    }

    fun updateTokenSize(sizeChange: Float, uuid: UUID) {
        _activeTokenList = _activeTokenList.mapIndexed { i, it ->
            if (it.uuid == uuid) {
                Log.d("updateTokenSize", "token size:${it.size}")
                it.copy( size = (_activeTokenList[i].size * (sizeChange).coerceIn(0.04F, 2F)) )
            }
            else it
        }
    }

    fun createToken(drawable: Int? = null) {
        if (_activeTokenList.isNotEmpty() ) { _activeTokenList += mutableListOf(
                Token(
                    drawableRes = drawable ?: R.drawable.minotaur_berserker,
                    size = _activeTokenList.last().size,
                    position = (_activeTokenList.last().position + Offset(10f, 10f))
                )
            )
        } else _activeTokenList = mutableListOf(Token(drawable!!))
    }

    fun deleteToken(uuid: UUID) {
        _activeTokenList = _activeTokenList.filter { it.uuid!= uuid }
    }

    fun duplicateToken(token: Token) {
        createToken(token.drawableRes)
    }

    fun setTokenInitiative (number: Int, uuid: UUID) {
        _activeTokenList = _activeTokenList.mapIndexed { index, token ->
            if (token.uuid == uuid)
                token.copy(currentInitiave = number)
            else
                token
        }
    }

    fun updateMapImageUri (uri: Uri? ) {
        backgroundMap.uri = uri
        backgroundMap.resId = null

    }

    fun updateMapResourceId (resource: Int) {
        backgroundMap.resId =  resource
        backgroundMap.uri = null
    }

    fun setPickerVisibility (state: Boolean) {
            isPickerVisible = state
    }

    fun updateUserAddedMapsList(){
        userAddedMapsList = fileHandler.getInternalStorageMapList()
    }
    fun updateUserAddedTokensList(){
        userAddedTokensList = fileHandler.getInternalStorageTokenList()
    }

    fun makeMapSelected () {
        backgroundMap = backgroundMap.copy( isSelected = true )
        _activeTokenList = _activeTokenList.map { it.copy( isSelected = false ) }
        Log.d("Selection", "isMapSelected: ${backgroundMap.isSelected}, isTokenSelected: ${_activeTokenList[0].isSelected}")
    }

    fun makeTokenSelected (selectedToken: Token) {
        _activeTokenList = _activeTokenList.mapIndexed { index, token ->
            if (token.uuid == selectedToken.uuid) token.copy( isSelected = true )
            else token.copy( isSelected = false )
        }
        backgroundMap = backgroundMap.copy( isSelected = false )
        Log.d("Selection", "isMapSelected: ${backgroundMap.isSelected}, isTokenSelected: ${_activeTokenList[0].isSelected}")
    }

    fun getSelectedTokenUuid (): UUID {
        return _activeTokenList.find { it.isSelected }?.uuid?: UUID.randomUUID()
    }

    fun everyoneRollForInitiative () {
        _activeTokenList.map {it.copy(
                 currentInitiave = Random.nextInt(1, 20) + it.initiativeModifier
            )
        }
        _activeTokenList.forEach {
            Log.d("Initiative", "${it.name}: ${it.currentInitiave}")
        }
    }

}
