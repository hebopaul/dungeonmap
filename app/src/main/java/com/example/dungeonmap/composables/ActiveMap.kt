package com.example.dungeonmap.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.dungeonmap.MainViewModel

//This is basically the main surface of the app
// i.e the image that acts as the playing board of the game
@Composable
fun ActiveMap(
    myModifier: Modifier,
    mVM: MainViewModel,
) {
    val map = mVM.backgroundMap
    val effectsList = mVM.visibleEffects.collectAsState(initial = null).value

    Box(
        modifier = Modifier
            .fillMaxSize(),

        ) {
        if ( map.uri == null )
            Image(
            //We use the connectedModifier that was declared inside the TerrainScreen() Composable
            modifier = myModifier,
            contentDescription = "Stock Image",
            painter = painterResource(
                id = map.resId!!
            )
        )
        else AsyncImage(
            model = map.uri!!,
            contentDescription = "Image from gallery",
            modifier = myModifier
        )


        if (mVM.effectCreatorIsVisible) EffectCreator(mVM)
    }

    Canvas( modifier = Modifier
        .fillMaxSize()
        .scale(mVM.globalScale)
    ){ effectsList?.forEach {effect -> effect.draw()()} }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
        {
            Tokens(mVM)
        }


    //Log.d(("Token drawn"), "token scale = ${token.value.scale}   token size = ${token.value.tokenSize}")
    TerrainUIOverlay(mVM)
}

