package com.example.dungeonmap.composables


import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.composables.visible_effects.CircleEffect
import com.example.dungeonmap.composables.visible_effects.LineEffect
import com.example.dungeonmap.composables.visible_effects.PolygonEffect
import com.example.dungeonmap.composables.visible_effects.RectangleEffect
import com.example.dungeonmap.data.VisibleEffectType
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
fun ConfirmEffectButtons(
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
