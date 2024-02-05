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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.utilities.toDp
import com.example.dungeonmap.utilities.tween
import kotlin.time.Duration.Companion.seconds

@Composable
fun TerrainScreen(mVM: MainViewModel) {

    val mapState by mVM.backgroundMap.collectAsState()

    val myModifier  = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, drag, zoom, _ ->
                mVM.updateMapOffset(drag)
                when (mapState.isSelected) {
                    true -> mVM.updateMapScale(zoom)
                    else -> mVM.updateTokenSize(zoom, mVM.getSelectedTokenUuid())
                }
            }
        }
        .offset(
            x = mapState.mapOffset.x.toDp,
            y = mapState.mapOffset.y.toDp
        )
        .scale(mapState.mapScale)
        .animateContentSize()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box {
            ActiveMap(myModifier, mVM)
            //MapPickerDrawer(mVM)
            AnimatedVisibility(
                visible = mapState.isPickerVisible,
                enter = slideInVertically (
                    initialOffsetY = { it },
                    animationSpec = tween(0.5.seconds)
                ),
                exit = slideOutVertically (
                    targetOffsetY = { it },
                    animationSpec = tween(0.5.seconds)
                ),
                content = { PickerDrawer(mVM) }
            )

            // NK: These type a closures, better to be explicitly set
            /*
               ComposableThingy(
                    someParam = someValue,
                    someParam = someValue,
                    someParam = someValue,
                    someParam = someValue
               ) { PickerDrawer() }

              Usually Better ==>
              ComposableThingy(
                    someParam = someValue,
                    someParam = someValue,
                    someParam = someValue,
                    someParam = someValue,
                    content = { PickerDrawer() }
               )
             */
        }
    }
}