package com.example.dungeonmap.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dungeonmap.MainViewModel

// This composable is used to add all the buttons etc.
// on the Terrain
@Composable
fun TerrainUIOverlay( mVM: MainViewModel ) {
    val mapState = mVM.backgroundMap.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ){

        Row {

            IconButton(
                onClick = {
                    mVM.setPickerVisible(true)
                },
                content = {
                    Icon(
                        imageVector = Icons.Outlined.AddCircleOutline,
                        contentDescription = "add item",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
            IconButton(
                onClick = { mVM.lockedScaleIconClicked() },
                content = {
                    Icon(
                        if (mapState.value.isScaleLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                        contentDescription = "Locked scale icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    }
}
