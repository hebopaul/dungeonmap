package com.example.dungeonmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dungeonmap.storage.FileHandler


class MainViewModelFactory(
    private val fileHandler: FileHandler
) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel (fileHandler) as T
    }
}