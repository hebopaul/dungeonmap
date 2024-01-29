package com.example.dungeonmap.composables

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dungeonmap.MainViewModel

// This composable is used to add all the buttons etc.
// on the Terrain
@Composable
fun TerrainUIOverlay(
    mVM: MainViewModel,
    mapPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {
    val mapState = mVM.backgroundMap.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ){
        Row {
            Button(
                onClick = {
                    mVM.setMapPickerState(true)
                },
                content = {
                    Text ("Select Map")
                }
            )
            Button(
                onClick = { mVM.setTokenPickerState(true) },
                content = {
                    Text("Add Token")
                }
            )

            IconButton(
                onClick = { mVM.lockedScaleIconClicked() },
                content = {
                    Icon(
                        if (mapState.value.isScaleLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                        contentDescription = "Locked scale icon",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            )

            Button(
                onClick = {
                    mapPickerLauncher.launch( PickVisualMediaRequest( ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                content = {
                    Text ("import image")
                }
            )

        }
    }
}
