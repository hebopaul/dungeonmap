package com.example.dungeonmap.composables.visible_effects

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import com.example.dungeonmap.data.Polygon

@Composable
fun PolygonEffect(mVM: MainViewModel) {
    var pointsList: List<Position> by remember { mutableStateOf(listOf()) }

    Box (modifier = Modifier
        .fillMaxSize()
        //.background(Color.White)
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                pointsList += Position(it.x, it.y)
                println("Point added: ${it.x} + ${it.y}")
            })
        }
        .pointerInput(Unit) {
            detectDragGestures { _, dragAmount ->
                mVM.updateGlobalPosition(dragAmount)
                pointsList = pointsList.map {
                    Position(
                        x = it.x + dragAmount.x,
                        y = it.y + dragAmount.y
                    )
                }
            }
        }
        .drawBehind(Polygon(points = pointsList).draw())
    ) {
        ConfirmEffectButtons(
            modifier = Modifier.align(Alignment.BottomEnd),
            onFinishedClicked = {
                mVM.addPolygonEffect (Position.Zero, pointsList )
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