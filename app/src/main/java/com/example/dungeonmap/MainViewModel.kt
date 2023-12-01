package com.example.dungeonmap

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










}
