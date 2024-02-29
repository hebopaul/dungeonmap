package com.example.dungeonmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dungeonmap.storage.FileHandler
import com.google.android.gms.nearby.connection.ConnectionsClient


class MainViewModelFactory(
    private val fileHandler: FileHandler,
    private val connectionsClient: ConnectionsClient
) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel (fileHandler, connectionsClient) as T
    }
}