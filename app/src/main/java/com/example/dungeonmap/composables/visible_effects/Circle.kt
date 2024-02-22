package com.example.dungeonmap.composables.visible_effects

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.input.pointer.pointerInput
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.Position
import com.example.dungeonmap.composables.ConfirmEffectButtons
import com.example.dungeonmap.data.Circle
import com.example.dungeonmap.toPosition

@Composable
fun CircleEffect(mVM: MainViewModel) {

    //start drawing a custom shape with the points from the pointsList
    var center: Position by remember { mutableStateOf(Position.Zero) }
    var radius: Float by remember { mutableStateOf(1F) }

    Box (modifier = Modifier
        .fillMaxSize()
        //.background(Color.White)
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { center = it.toPosition() }
            ) { _, dragAmount -> radius += (dragAmount.y) }
        }
        .pointerInput(Unit) {
            detectDragGestures { _, dragAmount ->
                mVM.updateGlobalPosition(dragAmount)
                center = Position(
                    x = center.x + dragAmount.x,
                    y = center.y + dragAmount.y
                )
            }
        }
        .drawBehind(Circle(center, radius).draw())
    ) {
        ConfirmEffectButtons(
            modifier = Modifier.align(Alignment.BottomEnd),
            onFinishedClicked = {
                mVM.addCircleEffect (position = center, radius)
                mVM.effectCreatorIsVisible = false
                mVM.uiIsVisible = true
                println("onFinishedClicked")
            },
            onCancelClicked = {
                mVM.effectCreatorIsVisible = false
                mVM.uiIsVisible = true
                println("onCancelClicked")
            }
        )
    }
}