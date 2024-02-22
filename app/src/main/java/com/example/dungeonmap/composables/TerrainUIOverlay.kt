package com.example.dungeonmap.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.Position
import com.example.dungeonmap.animated_effects.InvokeParticles
import com.example.dungeonmap.plus
import com.example.dungeonmap.toPosition
import com.example.dungeonmap.utilities.toDp
import kotlinx.coroutines.delay

// This composable is used to add all the buttons etc.
// on the Terrain
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TerrainUIOverlay( mVM: MainViewModel ) {

    //cool die animation
    val shake = remember { Animatable(0f) }
    var d20Clicked by remember { mutableStateOf(0L) }
    var pointerPosition by remember { mutableStateOf(Position.Zero)}
    var pointerIsDragged by remember { mutableStateOf(false)}
    
    LaunchedEffect(d20Clicked) {
        if (d20Clicked != 0L) {
            for (i in 0..10) {
                when (i % 2) {
                    0 -> shake.animateTo(5f, tween(80))
                    else -> shake.animateTo(-5f, tween(80))
                }
            }
            shake.animateTo(0f)
            d20Clicked = 0L
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(visible = mVM.battleOngoing) {
            InitiativeTracker(mVM)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        LaunchedEffect(pointerIsDragged) {
            if (!pointerIsDragged) delay (10000)
            mVM.animatedPointerStopped()
        }
        if (mVM.tempPointerEffect != null) InvokeParticles(position = pointerPosition, duration = 10000)


        if (mVM.uiIsVisible) Surface(
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(0.toDp, 750.toDp)
                .size(1000.toDp)
                .clip(CircleShape)
                .alpha(0.3F)
        ){}
        
        if (mVM.uiIsVisible) Row ( 
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.BottomCenter)
        ){

            IconButton(
                onClick = { mVM.setPickerVisibility(true) },
                content = { Icon(   imageVector = Icons.Outlined.AddCircleOutline,
                                    contentDescription = "add item",
                                    tint = Color.White                    )}
            )
            IconButton(
                onClick = { d20Clicked = System.currentTimeMillis()
                            mVM.rollForInitiative() },
                content = { Icon(   painter = painterResource(mVM.randomD20),
                                    contentDescription = "roll for initiative",
                                    tint = Color.Unspecified            )},
                modifier = Modifier
                    .scale(1.5F + animateFloatAsState(targetValue = if (d20Clicked != 0L) 2F else 0F).value)
                    .offset(x = 0.toDp, y = -30.toDp)
                    .rotate(shake.value)
            )
            IconButton(
                onClick = {
                    mVM.effectCreatorIsVisible = true
                    mVM.uiIsVisible = false                               },
                content = { 
                    Icon(   
                        imageVector = Icons.Filled.StarOutline,
                        contentDescription = "add item",
                        tint = Color.White,
                        modifier = Modifier
                            .draggable2D(
                                state = rememberDraggable2DState { delta ->
                                    pointerPosition += delta.toPosition()
                                },
                                onDragStarted = { startedPosition ->
                                    pointerPosition = startedPosition.toPosition()
                                    pointerIsDragged = true
                                },
                                onDragStopped = { pointerIsDragged = false }
                            ),
                    )
                }
            )
        }
    }
}

@Composable
fun RollForInitiative(mVM: MainViewModel) {

}