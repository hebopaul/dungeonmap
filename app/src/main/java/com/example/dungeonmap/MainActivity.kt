package com.example.dungeonmap

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
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

    val mVM: MainViewModel = viewModel()
    val mapState = mVM.backgroundMap.collectAsState()

    val connectedModifier  = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, dragChange, scaleChange, _ ->
                mVM.updateMapScale(scaleChange)
                mVM.updateMapOffset(dragChange)
            }
        }
        .offset(
            x = mapState.value.mapOffset.x.toDp,
            y = mapState.value.mapOffset.y.toDp
        )
        .scale(mapState.value.mapScale)
        .animateContentSize()


    Box(
        modifier = Modifier
    ) {
        Terrain(
            connectedModifier,
            mVM
        )
        mapPickerGrid(mVM)


    }
}

//This is basically the main surface of the app
// i.e the image that acts as the playing board of the game
@Composable
fun Terrain(
    modifier: Modifier,
    mVM: MainViewModel,
) {
    val token = mVM.token.collectAsState()
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
    ) {
        token.value.forEach { token ->
            Icon(
                painterResource(token.imageResource),
                token.name ?: "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .offset(
                        x = token.position.x.toDp,
                        y = token.position.y.toDp
                    )
                    .scale((token.scale * token.tokenSize))
                    .pointerInput(Unit) {

                        detectDragGestures { _, dragAmount ->
                            mVM.updateTokenOffset(dragAmount, token.tokenId)
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress { _, dragAmount ->
                            mVM.updateTokenSize(dragAmount.y, token.tokenId)
                        }
                    }
                    .animateContentSize()
            )
        }
            //Log.d(("Token drawn"), "token scale = ${token.value.scale}   token size = ${token.value.tokenSize}")
        TerrainUI(mVM, mapPickerLauncher)
    }
}


// This composable is used to add all the buttons etc.
// on the Terrain
@Composable
fun TerrainUI(
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
                    mapPickerLauncher.launch(
                        PickVisualMediaRequest( ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                content = {
                    Text ("Select Map")
                }
            )
            Button(
                onClick = { mVM.createToken()},
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
                        tint = Color.Black
                    )
                }
            )

        }
    }
}

//This composable creates an animated tab that rises from below the screen, that presents the user with a
//lazy grid of all the drawable that are returned by getDrawableResourcesIds() function
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mapPickerGrid( mVM: MainViewModel) {
    Box(
        modifier =Modifier
            .fillMaxSize()

    ) {
        Card(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .offset(y = 30.dp)
                .fillMaxSize(0.8F)
                .animateContentSize()
                .align(Alignment.BottomCenter)
                .border(10.dp, Color.Black),


        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.toDp),
                content = {
                    mVM.mapsList.forEach {
                        item {
                            Icon(painterResource(it.first),
                                it.second,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        mVM.updateMapImageResource(it.first)

                                    }


                            )
                        }
                    }
                }
            )

        }
    }
}


