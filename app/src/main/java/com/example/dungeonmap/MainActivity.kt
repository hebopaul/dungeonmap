package com.example.dungeonmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.dungeonmap.ui.theme.DungeonMapTheme
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DungeonMapTheme {
                DungeonMapApp()
                painterResource(R.drawable.m03_tombofhorrors_300)
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

    //Prepping the variables that will hold the state of
    //the map to enable dragging and zooming
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    //This enables the user to lock the zoom of the map
    var lockedScale by remember {mutableStateOf(false)}

    //Since there will be tokens representing characters on the map
    //we are going to need a shared modifier that keeps all the composables
    //that use it, moving as one, with each other, and with the terrain when
    //dragging or zooming on it.
    val connectedModifier  = Modifier
        .transformable( //This takes the gestures (dragging, pinching) from the user and updates their state
            state = rememberTransformableState { zoomChange, offsetChange, rotation ->
                if (lockedScale) scale = scale
                else scale *= zoomChange
                offset += offsetChange
            }
        )
        .graphicsLayer { //This alters the image position and scale
            scaleX = scale.coerceIn(0.4F, 10F)
            scaleY = scale.coerceIn(0.4F, 10F)
            translationX = offset.x
            translationY = offset.y
            println("Offset X = $translationX   -   Offset Y = $translationY   -   Scale = $scaleX")
        }
        .animateContentSize()

    Box(
        modifier = Modifier
    ) {
        Terrain(connectedModifier)

        TerrainUI(
            onLockedScaleClicked = {lockedScale = !lockedScale; println ("Locked scale = $lockedScale")}
        )
    }
}

//This is basically the main surface of the app
// i.e the image that acts as the playing board of the game
@Composable
fun Terrain(connectedModifier: Modifier) {



    Box(
        modifier = Modifier
            .fillMaxSize(),

        ) {
        Image(
            //We use the connectedModifier that was declared inside the TerrainScreen() Composable
            modifier = connectedModifier,
            contentDescription = "Imported image",
            painter = painterResource(R.drawable.m03_tombofhorrors_300)
        )
    }
    Box(
        modifier = Modifier
    ) {
        var tokenOffset by remember { mutableStateOf(Offset(0f, 0f))}
        Icon(
            painterResource(id = R.drawable.minotaur_berserker),
            "token",
            tint = Color.Unspecified,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress { _, dragAmount ->
                        val summed = tokenOffset + dragAmount
                        val newValue = Offset(
                            x = summed.x,
                            y = summed.y
                        )
                        tokenOffset = newValue
                    }
                }
                .graphicsLayer {
                    translationX = tokenOffset.x
                    translationY = tokenOffset.y
                }

        )
    }



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