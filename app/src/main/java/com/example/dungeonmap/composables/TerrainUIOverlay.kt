package com.example.dungeonmap.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.utilities.toDp

// This composable is used to add all the buttons etc.
// on the Terrain
@Composable
fun TerrainUIOverlay( mVM: MainViewModel ) {
    val mapState = mVM.backgroundMap.collectAsState()

    val shake      =  remember { Animatable(initialValue = 0f) }
    var d20Clicked by remember { mutableStateOf(false) }

    LaunchedEffect(d20Clicked) {
        if (d20Clicked) {
            repeat(10) {
                val target = if (it % 2 == 0) 5f else -5f
                shake.animateTo(target, tween(80))
            }

            shake.animateTo(0f)
            d20Clicked = false
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(horizontalArrangement = Arrangement.Center) {

            val scaleOffset by animateFloatAsState(
                targetValue = if (d20Clicked) 2F else 0F,
                label = "roll_animation"
            )

            IconButton(
                onClick = { mVM.setPickerVisible(true) },
                content = { Icon(
                    imageVector = Icons.Outlined.AddCircleOutline,
                    contentDescription = "add item",
                    tint = MaterialTheme.colorScheme.primary
                ) }
            )

            IconButton(
                onClick = { d20Clicked = true },
                content = { Icon(
                    painter = mVM.randomD20.painter,
                    contentDescription = "add item",
                    tint = Color.Unspecified
                ) },
                modifier = Modifier
                    .scale(1.5F + scaleOffset)
                    .offset(x = 0.toDp, y = (-30).toDp)
                    .rotate(shake.value)
            )

            IconButton(
                onClick = { mVM.lockedScaleIconClicked() },
                content = { Icon(
                    when (mapState.value.isScaleLocked) {
                        true -> Icons.Filled.Lock
                        else -> Icons.Filled.LockOpen
                    },
                    contentDescription = "Locked scale icon",
                    tint = MaterialTheme.colorScheme.primary
                ) }
            )
        }
    }
}

@Composable
fun RollForInitiative(mVM: MainViewModel) {

}