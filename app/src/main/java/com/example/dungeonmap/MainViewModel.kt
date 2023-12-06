package com.example.dungeonmap

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.MapState
import com.example.dungeonmap.data.TokenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class MainViewModel: ViewModel() {


    private val _mapState = MutableStateFlow(MapState())
    private val _tokenState = MutableStateFlow(TokenState())
    val mapState: StateFlow<MapState> = _mapState.asStateFlow()
    val tokenState: StateFlow<TokenState> = _tokenState.asStateFlow()

    fun updateMapOffset(newOffset: Offset) {
        _mapState.value = _mapState.value.copy(
            mapOffset = Offset(
                mapState.value.mapOffset.x + newOffset.x,
                mapState.value.mapOffset.y + newOffset.y
            )
        )
        updateTokenOffset(newOffset)
    }

    fun updateMapScale(newScale: Float) {
        _mapState.value = _mapState.value.copy(
            mapScale = newScale
        )
    }

    fun updateTokenOffset(newPosition: Offset) {
        _tokenState.value = _tokenState.value.copy(
            position = Offset(
                _tokenState.value.position.x + newPosition.x,
                _tokenState.value.position.y + newPosition.y
            )
        )
    }

    fun lockedScaleIconClicked() {
        _mapState.value = _mapState.value.copy(
            isScaleLocked = !mapState.value.isScaleLocked
        )
    }


























}
