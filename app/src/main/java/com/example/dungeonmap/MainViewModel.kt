package com.example.dungeonmap

import androidx.lifecycle.ViewModel
import com.example.dungeonmap.storage.FileHandler

const val MIN_SCALE: Float = 1F
const val MAX_SCALE: Float = 10F



class MainViewModel(val fileHandler: FileHandler) : ViewModel()
