package com.example.dungeonmap

import android.net.Uri
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

const val MAX_ZOOM_IN: Float = 1F
const val MAX_ZOOM_OUT: Float = 10F
var defaultTokenSize: Float? = null

class MainViewModel: ViewModel() {

    //Initializing the BackgroundMap and Token as MutableStateFlow objects
    private val _backgroundMap = MutableStateFlow(BackgroundMap())
    private val _token = MutableStateFlow(listOf(Token()))
    val backgroundMap: StateFlow<BackgroundMap> = _backgroundMap.asStateFlow()
    val token: StateFlow<List<Token>> = _token.asStateFlow()

    var mapImageUri: Uri? = null

    //Functions to update the BackgroundMap and Token
    fun updateMapOffset(newOffset: Offset) {
        _backgroundMap.value = _backgroundMap.value.copy(
            mapOffset = Offset(
                backgroundMap.value.mapOffset.x + newOffset.x,
                backgroundMap.value.mapOffset.y + newOffset.y
            )
        )
        _token.value = _token.value.mapIndexed { i, it ->
            it.copy(
                position = Offset(
                    token.value[i].position.x + newOffset.x,
                    token.value[i].position.y + newOffset.y
                )
            )
        }
    }

    //This function is called when the user pinches to zoom in or out
    fun updateMapScale(scaleChange: Float) {

        if (scaleChange != 0F && !_backgroundMap.value.isScaleLocked) {
            _backgroundMap.value = _backgroundMap.value.copy(
                mapScale = scaleChange * backgroundMap.value.mapScale.coerceIn(
                    MAX_ZOOM_IN,
                    MAX_ZOOM_OUT
                )
            )
            _token.value = _token.value.mapIndexed { i, it ->
                it.copy(
                    scale = scaleChange * token.value[i].scale.coerceIn(MAX_ZOOM_IN, MAX_ZOOM_OUT)
                )
            }
            //In order for the map to stay centered on the screen when zooming in or out, we need to
            //update the map offset as well
            if (_backgroundMap.value.mapScale < 10F && _backgroundMap.value.mapScale > 1F)
                _backgroundMap.value = _backgroundMap.value.copy(
                    mapOffset = Offset(
                        backgroundMap.value.mapOffset.x * scaleChange,
                        backgroundMap.value.mapOffset.y * scaleChange
                    )
                )
            //We also need to update the token offset so that it stays in the same position on the map
            if (_backgroundMap.value.mapScale < 10F && _backgroundMap.value.mapScale > 1F)
                _token.value = _token.value.mapIndexed { i, it ->
                    it.copy(
                        position = Offset(
                            token.value[i].position.x * scaleChange,
                            token.value[i].position.y * scaleChange
                        )
                    )
                }
        }


        Log.d(
            "updateMapScale called",
            "map offset = ${backgroundMap.value.mapOffset}" +
            "map scale = ${backgroundMap.value.mapScale}" +
            "token offset = ${token.value[0].position}" +
            "token scale = ${token.value[0].scale}"
        )
    }

    //This function is called when the user drags the token
    fun updateTokenOffset(newPosition: Offset, uuid: UUID) {

        _token.value = _token.value.mapIndexed { i, it ->
            if (it.tokenId == uuid)
                it.copy(
                    position = Offset(
                        _token.value[i].position.x + newPosition.x *
                                backgroundMap.value.mapScale * _token.value[i].tokenSize,
                        _token.value[i].position.y + newPosition.y *
                                backgroundMap.value.mapScale * _token.value[i].tokenSize
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
        _token.value = _token.value.mapIndexed { i, it ->
            if (it.tokenId == uuid)
                it.copy(
                    tokenSize = (token.value[i].tokenSize + (sizeChange * 0.001F)).coerceIn(0.04F, 2F)
                )
            else it
        }
    }

    fun createToken() {
        _token.value += mutableListOf(
            Token(
                tokenSize = token.value.last().tokenSize,
                scale = token.value.last().scale,
                position = token.value.last().position + Offset(10f, 10f)
            )
        )
    }


    fun giveMapImageUri (uri: Uri? ) { mapImageUri = uri }

}
