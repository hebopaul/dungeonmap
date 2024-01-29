package com.example.dungeonmap.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.MainViewModelFactory
import com.example.dungeonmap.utilities.toDp

@Composable
fun TerrainScreen() {
    val viewModelFactory = MainViewModelFactory()
    val mVM = viewModel<MainViewModel>(factory = viewModelFactory)


    val mapState = mVM.backgroundMap.collectAsState()

    val myModifier  = Modifier
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


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
        ) {
            ActiveMap(
                myModifier,
                mVM
            )

            MapPickerDrawer(mVM)
            TokenPickerDrawer(mVM)

        }
    }
}