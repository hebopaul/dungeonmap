package com.example.dungeonmap

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.dungeonmap.data.MapState
import com.example.dungeonmap.data.TokenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class MainViewModel: ViewModel() {

    init {
    }

    private val _mapState = MutableStateFlow(MapState())
    private val _tokenState = MutableStateFlow(TokenState())
    val mapState: StateFlow<MapState> = _mapState.asStateFlow()
    val tokenState: StateFlow<TokenState> = _tokenState.asStateFlow()


    fun setMapImage(drawable: Int) {
        _mapState.value.copy(
            imageResource = drawable
        )
    }

    fun setMapScale(newScale: Float) {
        _mapState.value.copy(
            mapScale = newScale
        )
    }

    fun lockedScaleIconClicked () {
        _mapState.value.copy(
            isScaleLocked = !_mapState.value.isScaleLocked
        )
    }

    fun moveMapBy(offset: Offset) {
        _mapState.value.copy(
            mapOffset = Offset(
                _mapState.value.mapOffset.x + offset.x,
                _mapState.value.mapOffset.y + offset.y
            )
        )
        _tokenState.value.moveCollectivePosition(offset)
    }



















}
