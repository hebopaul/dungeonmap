package com.example.dungeonmap.data

import android.net.Uri
import com.example.dungeonmap.R

data class BackgroundMap (
    var resId: Int? = R.drawable.map_tombofhorrors,
    var uri: Uri? = null,
    val isSelected: Boolean = true
)                                                        





