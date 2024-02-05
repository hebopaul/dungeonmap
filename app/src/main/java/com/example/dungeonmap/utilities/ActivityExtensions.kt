package com.example.dungeonmap.utilities

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

fun ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>.launchPhotos()
    = this.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
