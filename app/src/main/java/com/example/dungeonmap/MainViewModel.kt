package com.example.dungeonmap

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

const val MAX_ZOOM_IN: Float = 1F
const val MAX_ZOOM_OUT: Float = 10F
var defaultTokenSize: Float? = null

class MainViewModel: ViewModel() {

    //Initializing the BackgroundMap and Token as MutableStateFlow objects
    private val _backgroundMap = MutableStateFlow(BackgroundMap())
    private val _token = MutableStateFlow(Token())
    val backgroundMap: StateFlow<BackgroundMap> = _backgroundMap.asStateFlow()
    val token: StateFlow<Token> = _token.asStateFlow()



    //Functions to update the BackgroundMap and Token
    fun updateMapOffset(newOffset: Offset) {
        _backgroundMap.value = _backgroundMap.value.copy(
            mapOffset = Offset(
                backgroundMap.value.mapOffset.x + newOffset.x,
                backgroundMap.value.mapOffset.y + newOffset.y
            )
        )
        _token.value = _token.value.copy(
            position = Offset(
                token.value.position.x + newOffset.x,
                token.value.position.y + newOffset.y
            )
        )
    }

    //This function is called when the user pinches to zoom in or out
    fun updateMapScale(scaleChange: Float) {

        if (scaleChange != 0F && !_backgroundMap.value.isScaleLocked){
            _backgroundMap.value = _backgroundMap.value.copy(
                mapScale = scaleChange * backgroundMap.value.mapScale.coerceIn(MAX_ZOOM_IN, MAX_ZOOM_OUT)
            )
            _token.value = _token.value.copy(
                scale = scaleChange * token.value.scale.coerceIn(MAX_ZOOM_IN, MAX_ZOOM_OUT)
            )

            //In order for the map to stay centered on the screen when zooming in or out, we need to
            //update the map offset as well
            _backgroundMap.value = _backgroundMap.value.copy(
                mapOffset = Offset(
                    backgroundMap.value.mapOffset.x * scaleChange,
                    backgroundMap.value.mapOffset.y * scaleChange
                )
            )
            //We also need to update the token offset so that it stays in the same position on the map
            if (_backgroundMap.value.mapScale < 10F && _backgroundMap.value.mapScale > 1F)
                _token.value = _token.value.copy(
                position = Offset(
                    token.value.position.x * scaleChange,
                    token.value.position.y * scaleChange
                )
            )
        }


        Log.d(
            "updateMapScale called",
            "map offset = ${backgroundMap.value.mapOffset}" +
            "map scale = ${backgroundMap.value.mapScale}" +
            "token offset = ${token.value.position}" +
            "token scale = ${token.value.scale}"
        )
    }

    //This function is called when the user drags the token
    fun updateTokenOffset(newPosition: Offset) {
        _token.value = _token.value.copy(
            position = Offset(
                _token.value.position.x + newPosition.x *
                        backgroundMap.value.mapScale * _token.value.tokenSize,
                _token.value.position.y + newPosition.y *
                        backgroundMap.value.mapScale * _token.value.tokenSize
            )
        )
        Log.d("Token moved", "token offset = ${token.value.position}")
    }

    //This function is called when the user clicks the lock scale button
    fun lockedScaleIconClicked() {
        _backgroundMap.value = _backgroundMap.value.copy(
            isScaleLocked = !backgroundMap.value.isScaleLocked
        )
    }

    fun updateTokenSize(sizeChange: Float) {
        _token.value = _token.value.copy(
            tokenSize = (token.value.tokenSize + (sizeChange * 0.001F)).coerceIn(0.04F, 2F)
        )
    }

    fun setDefaultTokenSize(defaultSize: Float) {
        defaultTokenSize = defaultSize
    }

}
