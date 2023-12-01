package com.example.dungeonmap.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.example.dungeonmap.R

data class MapState (
    var imageResource: Int = R.drawable.m03_tombofhorrors_300,
    var mapOffset: Offset = Offset(0f, 0f),
    var mapScale: Float = 1F,
    var isScaleLocked: Boolean = false
) {
    fun setMap(drawable: Int) {
        imageResource = drawable
    }

    fun setScale(newScale: Float) {
        mapScale += newScale
    }

    fun lockedScaleIconClicked () {
        isScaleLocked = !isScaleLocked
    }

    fun moveBy(offset: Offset) {
        mapOffset = Offset(
            mapOffset.x + offset.x,
            mapOffset.y + offset.y
        )
        TokenState.collectivePosition = mutableStateOf(
            Offset(
                mapOffset.x + offset.x,
                mapOffset.y + offset.y
            )
        )
    }
}