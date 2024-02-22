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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.Position
import com.example.dungeonmap.composables.ConfirmEffectButtons
import com.example.dungeonmap.data.Rectangle
import com.example.dungeonmap.plus
import com.example.dungeonmap.toPosition

@Composable
fun RectangleEffect(mVM: MainViewModel) {

    //start drawing a custom shape with the points from the pointsList
    var startPos: Position by remember { mutableStateOf(Position.Zero) }
    var dimensions: Size by remember { mutableStateOf(Size.Zero) }

    Box (modifier = Modifier
        .fillMaxSize()
        //.background(Color.White)
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { startPos = it.toPosition()}
            ) { _, dragAmount -> dimensions =  Size(
                width  = dimensions.width  + dragAmount.x,
                height = dimensions.height + dragAmount.y
            )
            }
        }
        .pointerInput(Unit) {
            detectDragGestures { _, dragAmount ->
                mVM.updateGlobalPosition(dragAmount)
                startPos += dragAmount.toPosition()
            }
        }
        .drawBehind(
            Rectangle(startPos, dimensions).draw()
        )
    ) {
        ConfirmEffectButtons(
            modifier = Modifier.align(Alignment.BottomEnd),
            onFinishedClicked = {
                mVM.addRectangleEffect ( startPos, dimensions )
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