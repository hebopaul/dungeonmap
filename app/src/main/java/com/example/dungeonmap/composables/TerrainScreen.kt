package com.example.dungeonmap.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.utilities.toDp

@Composable
fun TerrainScreen(mVM: MainViewModel) {

    val map = mVM.backgroundMap

    val myModifier  = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, drag, zoom, _ ->
                if (zoom == 1F) mVM.updateMapPosition(drag)
                if (map.isSelected){
                    mVM.updateMapScale(zoom)
                }
                else {
                    mVM.updateTokenSize(zoom, mVM.getSelectedTokenUuid())
                }
            }
        }
        .offset(
            x = mVM.globalPosition.x.toDp,
            y = mVM.globalPosition.y.toDp
        )
        .scale(mVM.globalScale)
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
            //MapPickerDrawer(mVM)
            AnimatedVisibility(
                visible = mVM.isPickerVisible,
                enter = slideInVertically (
                    initialOffsetY = { it },
                    animationSpec = tween(500)
                ),
                exit = slideOutVertically (
                    targetOffsetY = { it },
                    animationSpec = tween(500)
                )
            ){ PickerDrawer(mVM) }

        }
    }
}