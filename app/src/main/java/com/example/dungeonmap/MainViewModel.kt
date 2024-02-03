package com.example.dungeonmap

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.StockImage
import com.example.dungeonmap.data.Token
import com.example.dungeonmap.storage.FileHandler
import com.example.dungeonmap.utilities.getDrawableResourcesIds
import com.example.dungeonmap.utilities.getStockImageList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import kotlin.random.Random

const val MIN_SCALE: Float = 1F
const val MAX_SCALE: Float = 10F



class MainViewModel(val fileHandler: FileHandler) : ViewModel() {

    //Initializing the BackgroundMap and Token as MutableStateFlow objects
    private val _backgroundMap = MutableStateFlow(BackgroundMap())
    private val _activeTokenList:MutableStateFlow<List<Token>> = MutableStateFlow(listOf(Token()))
    val backgroundMap: StateFlow<BackgroundMap> = _backgroundMap.asStateFlow()
    val activeTokenList: StateFlow<List<Token>> = _activeTokenList.asStateFlow()


    private val stockD20List: List<Int> = getDrawableResourcesIds("d20")
    val stockMapsList: List<StockImage> = getStockImageList("map")
    val stockTokensList: List<StockImage> = getStockImageList("token")
    var userAddedMapsList by mutableStateOf(fileHandler.getInternalStorageMapList())
    var userAddedTokensList by mutableStateOf(fileHandler.getInternalStorageTokenList())

    val randomD20 = stockD20List[Random.nextInt(stockD20List.size-1)]


    var mapImageUri: Uri? = null


    //Functions to update the BackgroundMap and Token position
    fun updateMapOffset(newOffset: Offset) {
        _backgroundMap.value = _backgroundMap.value.copy(
            mapOffset = Offset(
                backgroundMap.value.mapOffset.x + newOffset.x,
                backgroundMap.value.mapOffset.y + newOffset.y
            )
        )
        _activeTokenList.value = _activeTokenList.value.mapIndexed { i, it ->
            it.copy(
                position = Offset(
                    activeTokenList.value[i].position.x + newOffset.x,
                    activeTokenList.value[i].position.y + newOffset.y
                )
            )
        }
    }

    //This function is called when the user pinches to zoom in or out
    fun updateMapScale(scaleChange: Float) {

        if (scaleChange != 0F && !_backgroundMap.value.isScaleLocked) {
            _backgroundMap.value = _backgroundMap.value.copy(
                mapScale = scaleChange * backgroundMap.value.mapScale.coerceIn(
                    MIN_SCALE,
                    MAX_SCALE
                )
            )
            _activeTokenList.value = _activeTokenList.value.mapIndexed { i, it ->
                it.copy(
                    scale = scaleChange * activeTokenList.value[i].scale.coerceIn(MIN_SCALE, MAX_SCALE)
                )
            }
            //In order for the map to stay centered on the screen when zooming in or out, we need to
            //update the map offset as well
            if (_backgroundMap.value.mapScale < MAX_SCALE && _backgroundMap.value.mapScale > MIN_SCALE)
                _backgroundMap.value = _backgroundMap.value.copy(
                    mapOffset = Offset(
                        backgroundMap.value.mapOffset.x * scaleChange,
                        backgroundMap.value.mapOffset.y * scaleChange
                    )
                )
            //We also need to update the token offset so that it stays in the same position on the map
            if (_backgroundMap.value.mapScale < MAX_SCALE && _backgroundMap.value.mapScale > MIN_SCALE)
                _activeTokenList.value = _activeTokenList.value.mapIndexed { i, it ->
                    it.copy(
                        position = Offset(
                            activeTokenList.value[i].position.x * scaleChange,
                            activeTokenList.value[i].position.y * scaleChange
                        )
                    )
                }
        }


        Log.d(
            "updateMapScale called",
            "map offset = ${backgroundMap.value.mapOffset}" +
            "map scale = ${backgroundMap.value.mapScale}" +
            "token offset = ${activeTokenList.value[0].position}" +
            "token scale = ${activeTokenList.value[0].scale}"
        )
    }

    //This function is called when the user drags the token
    fun updateTokenOffset(newPosition: Offset, uuid: UUID) {

        _activeTokenList.value = _activeTokenList.value.mapIndexed { i, it ->
            if (it.uuid == uuid)
                it.copy(
                    position = Offset(
                        _activeTokenList.value[i].position.x + newPosition.x *
                                backgroundMap.value.mapScale * _activeTokenList.value[i].tokenSize,
                        _activeTokenList.value[i].position.y + newPosition.y *
                                backgroundMap.value.mapScale * _activeTokenList.value[i].tokenSize
                     )
                )
            else it
        }
    }

    //This function is called when the user clicks the lock scale button
    fun lockedScaleIconClicked() {
        _backgroundMap.value = _backgroundMap.value.copy(
            isScaleLocked = !backgroundMap.value.isScaleLocked
        )
    }

    fun updateTokenSize(sizeChange: Float, uuid: UUID) {
        _activeTokenList.value = _activeTokenList.value.mapIndexed { i, it ->
            if (it.uuid == uuid)
                it.copy(
                    tokenSize = (activeTokenList.value[i].tokenSize * ( sizeChange ).coerceIn(0.04F, 2F))
                )
            else it
        }
    }

    fun createToken(drawable: Int? = null) {

        if (_activeTokenList.value.isNotEmpty()){
            _activeTokenList.value += mutableListOf(
                Token(
                    drawableRes = drawable ?: R.drawable.minotaur_berserker,
                    tokenSize = activeTokenList.value.last().tokenSize,
                    scale = activeTokenList.value.last().scale,
                    position = (activeTokenList.value.last().position + Offset(10f, 10f))
                )
            )
        } else _activeTokenList.value = mutableListOf(Token(drawable!!))

    }

    fun deleteToken(uuid: UUID) {
        _activeTokenList.value = _activeTokenList.value.filter { it.uuid!= uuid }
    }

    fun duplicateToken(token: Token) {
        createToken(token.drawableRes)
    }

    fun setTokenInitiative (number: Int, uuid: UUID) {
        _activeTokenList.value = _activeTokenList.value.mapIndexed { index, token ->
            if (token.uuid == uuid)
                token.copy(currentInitiave = number)
            else
                token
        }
    }


    fun updateMapImageUri (uri: Uri? ) {
        mapImageUri = uri
    }

    fun updateMapImageResource (resource: Int) {
        _backgroundMap.value = _backgroundMap.value.copy(
            imageResource = resource
        )
        mapImageUri = null
    }

    fun setPickerVisible (state: Boolean) {
        _backgroundMap.value = _backgroundMap.value.copy(
            isPickerVisible = state
        )
    }

    fun updateUserAddedMapsList(){
        userAddedMapsList = fileHandler.getInternalStorageMapList()
    }
    fun updateUserAddedTokensList(){
        userAddedTokensList = fileHandler.getInternalStorageTokenList()
    }

    fun makeMapSelected () {
        _backgroundMap.value = _backgroundMap.value.copy(
            isSelected = true
        )
        _activeTokenList.value = _activeTokenList.value.map {
            it.copy(
                isSelected = false
            )
        }
    }

    fun makeTokenSelected (selectedToken: Token) {
        _activeTokenList.value = _activeTokenList.value.mapIndexed { index, token ->
            if (token.uuid == selectedToken.uuid)
                token.copy(
                    isSelected = true
                )
            else
                token.copy(
                    isSelected = false
                )
        }
        _backgroundMap.value = _backgroundMap.value.copy(
            isSelected = false
        )
    }

    fun getSelectedTokenUuid (): UUID {
        return _activeTokenList.value.find { it.isSelected }?.uuid?: UUID.randomUUID()
    }

    fun everyoneRollForInitiative () {
        _activeTokenList.value.map {
            it.copy(
                 currentInitiave = Random.nextInt(1, 20) + it.initiativeModifier
            )
        }
        _activeTokenList.value.forEach {
            Log.d("Initiative", "${it.name}: ${it.currentInitiave}")
        }
    }

}
