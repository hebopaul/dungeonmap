package com.example.dungeonmap

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.InternalStorageImage
import com.example.dungeonmap.data.StockImage
import com.example.dungeonmap.data.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

const val MIN_SCALE: Float = 1F
const val MAX_SCALE: Float = 10F



class MainViewModel : ViewModel() {




    //Initializing the BackgroundMap and Token as MutableStateFlow objects
    private val _backgroundMap = MutableStateFlow(BackgroundMap())
    private val _activeTokenList = MutableStateFlow(listOf(Token()))
    val backgroundMap: StateFlow<BackgroundMap> = _backgroundMap.asStateFlow()
    val activeTokenList: StateFlow<List<Token>> = _activeTokenList.asStateFlow()

    lateinit var stockMapsList: List<StockImage?>
    lateinit var stockTokensList: List<StockImage?>
    lateinit var userAddedMapsList: List<InternalStorageImage>
    lateinit var userAddedTokensList: List<InternalStorageImage>

    var isUserMapListChanged by mutableStateOf(false)
    var isUserTokenListChanged by mutableStateOf(false)

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
            if (it.tokenId == uuid)
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
            if (it.tokenId == uuid)
                it.copy(
                    tokenSize = (activeTokenList.value[i].tokenSize + (-sizeChange * 0.001F)).coerceIn(0.04F, 2F)
                )
            else it
        }
    }

    fun createToken(drawable: Int? = null) {
        _activeTokenList.value += mutableListOf(
            Token(
                imageResource = drawable ?: R.drawable.minotaur_berserker,
                tokenSize = activeTokenList.value.last().tokenSize,
                scale = activeTokenList.value.last().scale,
                position = activeTokenList.value.last().position + Offset(10f, 10f)
            )
        )
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

    fun triggerUserMapList() {
        isUserMapListChanged = true
    }

    fun triggerUserTokenList() {
        isUserTokenListChanged = true
    }

}
