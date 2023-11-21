package com.example.dungeonmap

import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dungeonmap.ui.theme.DungeonMapTheme
import com.example.dungeonmap.ui.utilities.toDp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DungeonMapTheme {
                TerrainScreen()
            }
        }
    }
}

@Composable
fun TerrainScreen() {

    // This view model lives as long as TerrainScreen
    // is on the view stack (alive but some times not..)
    val viewModel: MainViewModel = viewModel()

    //Prepping the variables that will hold the state of
    //the map to enable dragging and zooming


    //This enables the user to lock the zoom of the map
    var lockedScale by remember { mutableStateOf(false) }

    //Since there will be tokens representing characters on the map
    //we are going to need a shared modifier that keeps all the composables
    //that use it, moving as one, with each other, and with the terrain when
    //dragging or zooming on it.
    val connectedModifier  = Modifier
        .pointerInput(Unit) {
            detectDragGestures { _, dragAmount ->
                viewModel.updateWorldOffset(Size(
                    dragAmount.x.toInt(),
                    dragAmount.y.toInt()
                ))
            }
        }
        .graphicsLayer { // This alters the image position and scale
            scaleX = viewModel.worldScale
            scaleY = viewModel.worldScale
            translationX = viewModel.worldOffset.width.toFloat()
            translationY = viewModel.worldOffset.height.toFloat()
            println("Offset X = $translationX   -   Offset Y = $translationY   -   Scale = $scaleX")
        }
        .animateContentSize()

    Box(
        modifier = Modifier
    ) {
        Terrain(connectedModifier, viewModel)

        TerrainUI(
            onLockedScaleClicked = { lockedScale = !lockedScale; println ("Locked scale = $lockedScale") }
        )
    }
}

//This is basically the main surface of the app
// i.e the image that acts as the playing board of the game
@Composable
fun Terrain(modifier: Modifier, viewModel: MainViewModel) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            // We use the connectedModifier that was declared inside the TerrainScreen() Composable
            modifier = modifier,
            contentDescription = "Imported image",
            painter = painterResource(R.drawable.m03_tombofhorrors_300)
        )
    }

    TokenView(token = viewModel.playerToken) { offset ->
        viewModel.moveToken(viewModel.playerToken.id, offset)
    }
}

@Composable
fun TokenView(token: MainViewModel.Token, onMove: (Size) -> Unit) {
    Icon(
        painterResource(id = token.drawable),
        "token",
        tint = Color.Unspecified,
        modifier = Modifier
            .offset(
                token.position.x.toDp,
                token.position.y.toDp
            )
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress { _, dragAmount ->
                    onMove(Size(dragAmount.x.toInt(), dragAmount.y.toInt()))
                }
            }
    )
}


// This composable is used to add all the buttons etc.
// on the Terrain
@Composable
fun TerrainUI(
    onLockedScaleClicked: () -> Unit
) {
    var lockedIcon by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ){
        IconButton(
            onClick = {
                onLockedScaleClicked()
                lockedIcon = !lockedIcon
            },
            content = {
                Icon(
                    if (lockedIcon) Icons.Filled.Lock else Icons.Filled.LockOpen,
                    contentDescription = "Locked scale icon",
                    tint = Color.Black
                )
            }
        )
    }
}

@Preview
@Composable
fun testing () {
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