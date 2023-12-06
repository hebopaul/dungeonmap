package com.example.dungeonmap

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.BackgroundMap
import com.example.dungeonmap.data.Token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


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
        updateTokenOffset(newOffset)
    }

    //This function is called when the user pinches to zoom in or out
    fun updateMapScale(newScale: Float) {
        _backgroundMap.value = _backgroundMap.value.copy(
            mapScale = newScale * backgroundMap.value.mapScale.coerceIn(1F, 10F)
        )
        _token.value = _token.value.copy(
            scale = newScale * token.value.scale.coerceIn(1F, 10F)
        )
        //In order for the map to stay centered on the screen when zooming in or out, we need to
        //update the map offset as well
        val newOffset = Offset(
            backgroundMap.value.mapOffset.x * newScale,
            backgroundMap.value.mapOffset.y * newScale
        )
        _backgroundMap.value = _backgroundMap.value.copy(
            mapOffset = newOffset
        )
        //We also need to do the same for the token offset
        _token.value = _token.value.copy(
            position = Offset(
                token.value.position.x * newScale,
                token.value.position.y * newScale
            )
        )



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
                _token.value.position.x + newPosition.x,
                _token.value.position.y + newPosition.y
            )
        )
    }

    //This function is called when the user clicks the lock scale button
    fun lockedScaleIconClicked() {
        _backgroundMap.value = _backgroundMap.value.copy(
            isScaleLocked = !backgroundMap.value.isScaleLocked
        )
    }


























}
