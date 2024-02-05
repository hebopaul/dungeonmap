package com.example.dungeonmap.composables

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.data.Token
import com.example.dungeonmap.utilities.toDp


@Composable
fun Tokens(mVM: MainViewModel) {
    val tokenList = mVM.activeTokenList

    tokenList.forEach { token ->
        TokenBox( mVM, token )
    }
}

@Composable
fun TokenBox (mVM: MainViewModel, token: Token) {
    Box(
        modifier = Modifier
            .size(500.toDp)
            .offset(
                x = token.position.x.toDp,
                y = token.position.y.toDp
            ),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .clip(CircleShape)
                .alpha(if (token.isSelected) 0.3F else 0F)
                .size (animateDpAsState(targetValue =
                    if (token.isSelected) 450.toDp
                    else 0.toDp,
                    animationSpec = tween(500),
                    label = "shade animation"
                ).value)
        ){}
        AnimatedVisibility(
            visible = token.isSelected,
            enter = slideIn(initialOffset = {
                IntOffset(it.width * 2, it.height * 2)
            }, animationSpec = tween(500)),
            exit = slideOut(targetOffset = {
                IntOffset(it.width * 2, it.height * 2)
            }, animationSpec = tween(500))
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "delete",
                modifier = Modifier
                    .offset(x = (-150).toDp, y = (-100).toDp)
                    .clickable { mVM.deleteToken(token.uuid) },
                tint = MaterialTheme.colorScheme.error
            )
        }
        AnimatedVisibility(
            visible = token.isSelected,
            enter = slideIn(initialOffset = {
                IntOffset(0, it.height * 2)
            }, animationSpec = tween(500, 100)),
            exit = slideOut(targetOffset = {
                IntOffset(0, it.height * 2)
            }, animationSpec = tween(400, 100))
        ) {
            Icon(
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = "duplicate",
                modifier = Modifier
                    .offset(x = 0.toDp, y = (-150).toDp)
                    .clickable { mVM.duplicateToken(token) }
            )
        }
        AnimatedVisibility(
            visible = token.isSelected,
            enter = slideIn(initialOffset = {
                IntOffset(-it.width * 2, it.height * 2)
            }, animationSpec = tween(500, 200)),
            exit = slideOut(targetOffset = {
                IntOffset(-it.width * 2, it.height * 2)
            }, animationSpec = tween(500, 200))
        ) {
            Icon(
                imageVector = Icons.Filled.AvTimer,
                contentDescription = "initiative",
                modifier = Modifier
                    .offset(x = 150.toDp, y = (-100).toDp)
                    .clickable { mVM.setTokenInitiative(10, token.uuid) }
            )
        }

        SingleToken( mVM, token)
    }
}

@Composable
fun SingleToken( mVM: MainViewModel, token: Token) {

    Icon(
        painterResource(token.drawableRes),
        token.name ?: "",
        tint = Color.Unspecified,
        modifier = Modifier
            .scale((token.scale * token.tokenSize))
            .border(
                width = animateDpAsState(
                    targetValue = if (token.isSelected) 20.toDp
                    else 0.toDp,
                    animationSpec = tween(800),
                    label = "border animation"
                ).value,
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            .pointerInput(Unit) {

                detectTransformGestures { _, drag, zoom, _ ->
                    if(zoom == 1F && token.isSelected){
                        mVM.updateTokenSize(zoom, token.uuid)
                        Log.d("token zoom", zoom.toString())
                    }
                    else mVM.updateTokenOffset(drag, token.uuid)

                }
            }
            /*.pointerInput(Unit) {
                detectDragGesturesAfterLongPress { _, dragAmount ->

                }
            }*/
            .clickable(
                remember { MutableInteractionSource() },
                indication = null
            ) { mVM.makeTokenSelected(token) }

    )
}











