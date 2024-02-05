package com.example.dungeonmap.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

// This should probably not be in the models folder,
// But could be in extension for each class that provides a painter
interface ImageRepresentable {
    val painter: Painter
        @Composable get

    val name: String
}


data class InternalStorageImage(
    override val painter: Painter,
    override val name: String,
    val uri: String
): ImageRepresentable


data class StockImage(
    val id: Int,
    override val name: String
): ImageRepresentable {
    override val painter: Painter
        @Composable
        get() = painterResource(id = id)
}
