package com.example.dungeonmap.composables


import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Polyline
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Rectangle
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.Position
import com.example.dungeonmap.data.Circle
import com.example.dungeonmap.data.Line
import com.example.dungeonmap.data.Polygon
import com.example.dungeonmap.data.Rectangle
import com.example.dungeonmap.data.VisibleEffectType
import com.example.dungeonmap.plus
import com.example.dungeonmap.toPosition
import com.example.dungeonmap.utilities.toDp

@Composable
fun EffectCreator(mVM: MainViewModel) {
    var effectType: VisibleEffectType by remember { mutableStateOf(VisibleEffectType.Rectangle)}

    when (effectType) {
        VisibleEffectType.Rectangle -> RectangleEffect(mVM)
        VisibleEffectType.Circle -> CircleEffect(mVM)
        VisibleEffectType.Polygon -> PolygonEffect(mVM)
        VisibleEffectType.Line -> LineEffect(mVM)
    }

    Box (modifier = Modifier.fillMaxSize()){
        if( mVM.effectCreatorIsVisible) EffectTypesButtons(
            modifier = Modifier.align(Alignment.BottomStart),
            onPolygonClicked = { effectType = VisibleEffectType.Polygon },
            onCircleClicked = { effectType = VisibleEffectType.Circle },
            onRectangleClicked = { effectType = VisibleEffectType.Rectangle },
            onLineClicked = { effectType = VisibleEffectType.Line },
            selectedEffectType = effectType
        )
    }
}



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
       EffectCreatorButtons(
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

@Composable
fun CircleEffect(mVM: MainViewModel) {

    //start drawing a custom shape with the points from the pointsList
    var center: Position by remember { mutableStateOf(Position.Zero) }
    var radius: Float by remember { mutableStateOf(1F)}

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
        EffectCreatorButtons(
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
        EffectCreatorButtons(
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

@Composable
fun LineEffect(mVM: MainViewModel) {

    //start drawing a custom shape with the points from the pointsList
    var startPos: Position by remember { mutableStateOf(Position.Zero) }
    var offset: Offset by remember { mutableStateOf(Offset.Zero) }

    Box (modifier = Modifier
        .fillMaxSize()
        //.background(Color.White)
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { startPos = it.toPosition()}
            ) { _, dragAmount -> offset += dragAmount }
        }
        .pointerInput(Unit) {
            detectDragGestures { _, dragAmount ->
                mVM.updateGlobalPosition(dragAmount)
                startPos += dragAmount.toPosition()
            }
        }
        .drawBehind(
            Line(startPos, offset).draw()
        )
    ) {
        EffectCreatorButtons(
            modifier = Modifier.align(Alignment.BottomEnd),
            onFinishedClicked = {
                mVM.addLineEffect ( startPos, offset )
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

@Composable
fun EffectCreatorButtons(
    modifier: Modifier = Modifier,
    onFinishedClicked: () -> Unit,
    onCancelClicked: () -> Unit
){

    Row(modifier = modifier) {
        Surface(
            modifier = Modifier
                .alpha(0.7F)
                .padding(50.toDp), color = Color.Black, shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircleOutline,
                contentDescription = "Finished",
                tint = Color.White,
                modifier = Modifier.clickable { onFinishedClicked() }
            )
        }
        Surface(
            modifier = Modifier
                .alpha(0.7F)
                .padding(50.toDp), color = Color.Black, shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Sharp.Cancel,
                contentDescription = "Cancel",
                tint = Color.Red,
                modifier = Modifier.clickable { onCancelClicked() }
            )
        }
    }
}


@Composable
fun EffectTypesButtons (
    modifier: Modifier = Modifier,
    onPolygonClicked: () -> Unit,
    onCircleClicked: () -> Unit,
    onRectangleClicked: () -> Unit,
    onLineClicked: () -> Unit,
    selectedEffectType: VisibleEffectType
    ) {
    Surface(
        modifier.alpha(0.55F).offset(-15.toDp, 15.toDp),
        color = Color.Black,
        shape = RoundedCornerShape(15.toDp),

        ) {
        Row(modifier.padding(50.toDp)) {
            Icon(
                imageVector = Icons.Filled.Polyline,
                "Polygon",
                Modifier.clickable { onPolygonClicked() },
                tint = if (selectedEffectType == VisibleEffectType.Polygon) Color.White else Color.Gray
            )
            Icon(
                imageVector = Icons.Rounded.Circle,
                "Circle",
                Modifier.clickable { onCircleClicked()
                                   println("onCircleClicked")},
                tint = if (selectedEffectType == VisibleEffectType.Circle) Color.White else Color.Gray
            )
            Icon(
                imageVector = Icons.Rounded.Rectangle,
                "Rectangle",
                Modifier.clickable { onRectangleClicked()
                    println("onRectangleClicked") },
                tint = if (selectedEffectType == VisibleEffectType.Rectangle) Color.White else Color.Gray
            )
            Icon(
                imageVector = Icons.Default.LinearScale,
                "Line",
                Modifier.clickable { onLineClicked()
                    println("onLineClicked")},
                tint = if (selectedEffectType == VisibleEffectType.Line) Color.White else Color.Gray
            )
        }
    }
}
