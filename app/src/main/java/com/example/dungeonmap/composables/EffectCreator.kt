package com.example.dungeonmap.composables


import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.Position
import com.example.dungeonmap.data.Polygon
import com.example.dungeonmap.plus
import com.example.dungeonmap.utilities.toDp

@Composable
fun EffectCreator(mVM: MainViewModel, whatShape: String) {

    //start drawing a custom shape with the points from the pointsList
    var tempPointsList: List<Position> by remember { mutableStateOf(listOf()) }
    Box(modifier = Modifier
        .fillMaxSize()
        .polygonEffect(mVM)


    ){
        Row(modifier = Modifier.align(Alignment.BottomEnd)) {
            Surface(modifier = Modifier
                .alpha(0.7F)
                .padding(50.toDp), color = Color.Black, shape = CircleShape){
                Icon(
                    imageVector = Icons.Filled.CheckCircleOutline,
                    contentDescription = "Finished",
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        mVM.addPolygonEffect (position = mVM.globalPosition, points = tempPointsList)
                        mVM.effectCreatorIsVisible = false
                        mVM.uiIsVisible = true
                    }
                )
            }
            Surface(modifier = Modifier
                .alpha(0.7F)
                .padding(50.toDp), color = Color.Black, shape = CircleShape){
                Icon(
                    imageVector = Icons.Sharp.Cancel,
                    contentDescription = "Cancel",
                    tint = Color.Red,
                    modifier = Modifier.clickable {
                        mVM.effectCreatorIsVisible = false
                        mVM.uiIsVisible = true
                    }
                )
            }
        }
    }
}

@Composable
fun PolygonEffect(mVM: MainViewModel) {

    //start drawing a custom shape with the points from the pointsList
    var pointsList: List<Position> by remember { mutableStateOf(listOf()) }

    Box(modifier = Modifier
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
                        x = it.x + dragAmount.x * mVM.globalScale,
                        y = it.y + dragAmount.y * mVM.globalScale
                    )
                }
            }
        }
        .drawBehind {
            drawPath(
                path = Path().apply {
                    if (pointsList.isNotEmpty()) moveTo(pointsList[0].x, pointsList[0].y)
                    if (pointsList.size > 1) pointsList
                        .drop(1)
                        .forEach { point ->
                            lineTo(point.x, point.y)
                        }
                    //if (pointsList.size > 2) lineTo(pointsList[0].x, pointsList[0].y)
                },
                color = Color.Blue,
                style = Fill
            )
        }
    )
}

@Composable
fun Modifier.polygonEffect(mVM: MainViewModel): Modifier {
    var tempPointsList: List<Position> by remember { mutableStateOf(listOf()) }
    var polygon by remember { mutableStateOf(Polygon())}

    return this then pointerInput(Unit) {
        detectTapGestures(onTap = {
            polygon.addPoint(Position(it.x, it.y))
            println("Point added: ${it.x} + ${it.y}")
        })
    }
        .pointerInput(Unit) {
            detectDragGestures { _, dragAmount ->
                mVM.updateGlobalPosition(dragAmount)
                polygon = polygon.copy ( points = polygon.points.map {
                    Position(
                        x = it.x + dragAmount.x * mVM.globalScale,
                        y = it.y + dragAmount.y * mVM.globalScale
                    )
                }
                )
            }
        }
        .drawBehind {
            polygon.draw
        }
}

@Composable
fun Modifier.circleEffect(mVM: MainViewModel) {
    var center by remember { mutableStateOf( Position.Zero) }
    var radius by remember { mutableStateOf(1F) }
    pointerInput(Unit) {
        detectTapGestures(onTap = {
            center = mVM.globalPosition + it
        })
    }
    .pointerInput(Unit) {
        detectDragGestures { _, dragAmount ->
            radius *= dragAmount.y * 0.2F
        }
    }
}