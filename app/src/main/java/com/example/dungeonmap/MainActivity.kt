package com.example.dungeonmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dungeonmap.ui.theme.DungeonMapTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DungeonMapTheme {
                DungeonMapApp()
            }
        }
    }
}


@Composable
fun DungeonMapApp() {
    TerrainScreen()
}

@Composable
fun TerrainScreen() {

    val mainViewModel: MainViewModel = viewModel()
    val mapState = mainViewModel.mapState.collectAsState()

    val connectedModifier  = Modifier
        .transformable( //This takes the gestures (dragging, pinching) from the user and updates their state
            state = rememberTransformableState { scaleChange, offsetChange, _ ->

                mainViewModel.updateMapOffset(offsetChange)
                if (!mapState.value.isScaleLocked) {
                    mainViewModel.updateMapScale(scaleChange)
                }
            }
        )
        .graphicsLayer { //This alters the image position and scale

            scaleX = mapState.value.mapScale.coerceIn(0.4F, 10F)
            scaleY = mapState.value.mapScale.coerceIn(0.4F, 10F)
            translationX = mapState.value.mapOffset.x
            translationY = mapState.value.mapOffset.y
            println("Offset X = $translationX   -   Offset Y = $translationY   -   Scale = $scaleX")
        }
        .animateContentSize()

    Box(
        modifier = Modifier
    ) {
        Terrain(
            connectedModifier,
            mainViewModel
        )

        TerrainUI(mainViewModel)
    }
}

//This is basically the main surface of the app
// i.e the image that acts as the playing board of the game
@Composable
fun Terrain(
    modifier: Modifier,
    mainViewModel: MainViewModel
) {
    val token = mainViewModel.tokenState.collectAsState()
    val map = mainViewModel.mapState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize(),

        ) {
        Image(
            //We use the connectedModifier that was declared inside the TerrainScreen() Composable
            modifier = modifier,
            contentDescription = "Imported image",
            painter = painterResource(
                id = map.value.imageResource
            )
        )
    }
    Box(
        modifier = Modifier
    ) {

        Icon(
            painterResource(token.value.imageResource),
                token.value.name ?: "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectDragGestures { _, dragAmount ->
                            mainViewModel.updateTokenOffset(dragAmount)
                        }
                    }
                    .graphicsLayer {
                        translationX = mainViewModel.tokenState.value.position.x
                        translationY = mainViewModel.tokenState.value.position.y
                    }

        )
    }



}


// This composable is used to add all the buttons etc.
// on the Terrain
@Composable
fun TerrainUI(
    mainViewModel: MainViewModel
) {
    val mapState = mainViewModel.mapState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ){
        IconButton(
            onClick = { mainViewModel.lockedScaleIconClicked()},
            content = {
                Icon(
                    if (mapState.value.isScaleLocked) Icons.Filled.Lock else Icons.Filled.LockOpen,
                    contentDescription = "Locked scale icon",
                    tint = Color.Black
                )
            }
        )
    }
}

@Preview
@Composable
fun Testing () {
    Box(
        modifier = Modifier
            .fillMaxHeight(),
        contentAlignment = Alignment.BottomEnd

    ){
        Image(
            painterResource (R.drawable.m03_tombofhorrors_300), ""
        )
        IconButton(

            onClick = { /*TODO*/ },
            content = {
                Icon(Icons.Filled.Lock, "")
            }
        )
    }
}