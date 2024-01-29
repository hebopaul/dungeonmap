package com.example.dungeonmap.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.dungeonmap.MainViewModel

//This is basically the main surface of the app
// i.e the image that acts as the playing board of the game
@Composable
fun ActiveMap(
    modifier: Modifier,
    mVM: MainViewModel,
) {

    val map = mVM.backgroundMap.collectAsState()

    val mapPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { mVM.giveMapImageUri(it)}
    )
    Box(
        modifier = Modifier
            .fillMaxSize(),

        ) {
        if ( mVM.mapImageUri == null ) Image(
            //We use the connectedModifier that was declared inside the TerrainScreen() Composable
            modifier = modifier,
            contentDescription = "Stock Image",
            painter = painterResource(
                id = map.value.imageResource
            )
        )
        else AsyncImage(
            model = mVM.mapImageUri,
            contentDescription = "Image from gallery",
            modifier = modifier
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
        {
        Tokens(mVM)
        }


    //Log.d(("Token drawn"), "token scale = ${token.value.scale}   token size = ${token.value.tokenSize}")
    TerrainUIOverlay(mVM, mapPickerLauncher)
}

