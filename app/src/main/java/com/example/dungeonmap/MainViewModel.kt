package com.example.dungeonmap

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.Circle
import com.example.dungeonmap.data.Line
import com.example.dungeonmap.data.PointerEffect
import com.example.dungeonmap.data.Polygon
import com.example.dungeonmap.data.Rectangle
import com.example.dungeonmap.data.StockImage
import com.example.dungeonmap.data.Token
import com.example.dungeonmap.data.VisibleEffect
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.utilities.getDrawableResourcesIds
import com.example.dungeonmap.utilities.getNameFromResId
import com.example.dungeonmap.utilities.getStockImageList
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random

const val MIN_SCALE: Float = 1F
const val MAX_SCALE: Float = 10F
const val X_BOUNDARY: Float = 350F
const val Y_BOUNDARY: Float = 500F

class MainViewModel(val fileHandler: FileHandler) : ViewModel() {

    //Loading all of the image resources we are going to need
    val stockMapsList: List<StockImage> = getStockImageList("map")
    private val stockD20List: List<Int> = getDrawableResourcesIds("d20")
    val stockTokensList: List<StockImage> = getStockImageList("token")


    //This is where we keep our global variables
    var globalPosition by mutableStateOf(Position(0F, 0F))
        private set
    var globalScale by mutableFloatStateOf(1F)
        private set

    var pickerIsVisible by mutableStateOf(false)
    var effectCreatorIsVisible by mutableStateOf(false)
    var uiIsVisible by mutableStateOf(true)
    var userAddedMapsList by mutableStateOf(fileHandler.getInternalStorageMapList())
        private set
    var userAddedTokensList by mutableStateOf(fileHandler.getInternalStorageTokenList())
        private set
    var backgroundMap by mutableStateOf(BackgroundMap())
        private set
    var _activeTokens: List<Token> by mutableStateOf(listOf())
        private set
    var battleOngoing by mutableStateOf(false)
        private set
    var currentToken by mutableStateOf(0)
        private set
    var _visibleEffects: List<VisibleEffect> by mutableStateOf(listOf(Circle()))
        private set

    val visibleEffects = snapshotFlow {
        _visibleEffects.map { effect ->
            when (effect) {
                is Circle -> effect.copy(globalPosition + (effect.position * globalScale))
                is Rectangle -> effect.copy(globalPosition + (effect.position * globalScale))
                is Polygon -> effect.copy(globalPosition + (effect.position * globalScale))
                is Line -> effect.copy(globalPosition + (effect.position * globalScale))
                is PointerEffect -> effect.copy(globalPosition + (effect.position * globalScale))
            }
        }
    }
    var tempPointerEffect: PointerEffect? by mutableStateOf(null)
    val activeTokens = snapshotFlow{
        _activeTokens.map { token -> token.copy(position = globalPosition + (token.position * globalScale))}
    }

    val randomD20 = stockD20List[Random.nextInt(stockD20List.size-1)]


    fun updateGlobalPosition(newOffset: Offset) {
        globalPosition += newOffset
    }

    //This function is called when the user pinches to zoom in or out
    fun updateGlobalScale(scaleChange: Float) {
        globalScale = (globalScale * scaleChange).coerceIn(MIN_SCALE, MAX_SCALE)
        globalPosition = (globalPosition * scaleChange)/*.coerceAtMostScalable(
                                                            X_BOUNDARY, Y_BOUNDARY, globalScale
                                                        )*/
        if(_activeTokens.isNotEmpty())
            Log.d("Moved token","globalScale:${globalScale} "+"global: $globalPosition "+"token: ${_activeTokens[0].position}")
    }

    //This function is called when the user drags the token
    fun updateTokenPosition(newPosition: Offset, uuid: UUID) {
        Log.d("Moved token","globalScale:${globalScale} "+"global: $globalPosition "+"token: ${_activeTokens[0].position}")
        _activeTokens = _activeTokens.map { token ->
            if (token.uuid == uuid) token.copy(
                    position = token.position + newPosition / globalScale
                )
            else token
        }
    }

    fun updateTokenSize(sizeChange: Float, uuid: UUID) {
        _activeTokens = _activeTokens.mapIndexed { i, it ->
            if (it.uuid == uuid) {
                Log.d("updateTokenSize", "token size:${it.size}")
                it.copy( size = (_activeTokens[i].size * (sizeChange).coerceIn(0.04F, 2F)) )
            }
            else it
        }
    }

    fun createToken(drawable: Int? = null) {
        if (_activeTokens.isNotEmpty() ) { _activeTokens += mutableListOf(
                Token(
                    drawableRes = drawable ?: R.drawable.minotaur_berserker,
                    size = _activeTokens.last().size,
                    position = (_activeTokens.last().position + Offset(10f, 10f)),
                    name = getNameFromResId(drawable!!)
                )
            )
        } else _activeTokens = mutableListOf(Token(drawable!!))
    }

    fun deleteToken(uuid: UUID) {
        _activeTokens = _activeTokens.filter { it.uuid!= uuid }
    }

    fun duplicateToken(token: Token) {
        createToken(token.drawableRes)
    }

    fun setTokenInitiative (number: Int, uuid: UUID) {
        _activeTokens = _activeTokens.mapIndexed { index, token ->
            if (token.uuid == uuid)
                token.copy(currentInitiative = number)
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
            pickerIsVisible = state
    }

    fun updateUserAddedMapsList(){
        userAddedMapsList = fileHandler.getInternalStorageMapList()
    }
    fun updateUserAddedTokensList(){
        userAddedTokensList = fileHandler.getInternalStorageTokenList()
    }

    fun makeMapSelected () {
        backgroundMap = backgroundMap.copy( isSelected = true )
        _activeTokens = _activeTokens.map { it.copy( isSelected = false ) }
        if(_activeTokens.isNotEmpty())
            Log.d("Selection", "isMapSelected: ${backgroundMap.isSelected}, isTokenSelected: ${_activeTokens[0].isSelected}")
    }

    fun makeTokenSelected (selectedToken: Token) {
        _activeTokens = _activeTokens.mapIndexed { index, token ->
            if (token.uuid == selectedToken.uuid) token.copy( isSelected = true )
            else token.copy( isSelected = false )
        }
        backgroundMap = backgroundMap.copy( isSelected = false )
        if(_activeTokens.isNotEmpty())
            Log.d("Selection", "isMapSelected: ${backgroundMap.isSelected}, isTokenSelected: ${_activeTokens[0].isSelected}")
    }

    fun getSelectedTokenUuid (): UUID {
        return _activeTokens.find { it.isSelected }!!.uuid
    }

    fun rollForInitiative () {
        _activeTokens = _activeTokens.map {
            val dieCast = Random.nextInt(1, 20)
            println("Rolling for: $dieCast")
            it.copy(
                 currentInitiative =  dieCast + it.initiativeModifier
            )
        }.sortedByDescending { it.currentInitiative + (it.initiativeModifier/10F) }
        _activeTokens.forEach {
            Log.d("Initiative", "${it.name}: ${it.currentInitiative}")
        }
        //We start with the second occurrence of the token with the highest initiative
        currentToken = _activeTokens.size
        battleOngoing = true

    }

    fun nextTokenClicked (endOfList: Boolean) {
        when (endOfList) {
            false -> currentToken++
            true -> currentToken %= _activeTokens.size
        }
    }

    fun endBattle () {
        battleOngoing = false
    }

    fun previousTokenClicked(startOfList: Boolean) {
        when (startOfList) {
            false -> currentToken--
            true -> currentToken += _activeTokens.size
        }
    }

    fun addCircleEffect(position: Position, radius: Float) {
        _visibleEffects += mutableListOf( Circle(position, radius) )
    }

    fun addRectangleEffect(position: Position, dimensions: Size) {
        _visibleEffects += mutableListOf( Rectangle(position, dimensions) )
    }

    fun addPolygonEffect(position: Position, points: List<Position>) {
        _visibleEffects += mutableListOf( Polygon(position, points) )
    }

    fun addLineEffect(position: Position, offset: Offset) {
        _visibleEffects += mutableListOf( Line(position, offset) )
    }

    suspend fun createPointerEffect(position: Position) {
        tempPointerEffect = PointerEffect(position)
        delay(10000)
        tempPointerEffect = null
    }

}
