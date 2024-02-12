package com.example.dungeonmap.composables

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.data.Token
import com.example.dungeonmap.ui.theme.celestia
import com.example.dungeonmap.utilities.toDp


@Composable
fun Tokens(mVM: MainViewModel) {
    mVM.activeTokenList.collectAsState( initial = null).value?.forEach {
        TokenBox(mVM, it)
    }

}
val tokenBoxSize = 200
@Composable
fun TokenBox (mVM: MainViewModel, token: Token) {
    Box(
        modifier = Modifier
            .offset(
                x = token.position.x.toDp,
                y = token.position.y.toDp
            )
            .size( animateDpAsState( targetValue =
            if (token.size < 0.4F) tokenBoxSize.toDp else (tokenBoxSize/0.4F * token.size).toDp ).value
            ),
        contentAlignment = Alignment.Center
    ) {
        val boxAnim by  animateDpAsState( targetValue =
            if (token.size < 0.4F) (tokenBoxSize * mVM.globalScale).toDp
            else (tokenBoxSize/0.4F * token.size * mVM.globalScale).toDp, label = ""
        )

        Surface(
            color = Color.Black,
            modifier = Modifier
                .clip(CircleShape)
                .alpha(if (token.isSelected) 0.4F else 0F)
                .size( animateDpAsState( targetValue = if (token.isSelected) boxAnim else 0.toDp,
                                        animationSpec = tween(500),
                                        label = "shade animation").value
                )
        ){}
        AnimatedVisibility(
            visible = token.isSelected,
            enter = slideIn(initialOffset = {IntOffset(it.width * 2, it.height * 2)},
            animationSpec = tween(500)),
            exit = slideOut(targetOffset = {IntOffset(it.width * 2, it.height * 2)},
            animationSpec = tween(500))
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = "delete",
                modifier = Modifier
                    .offset(x = (-boxAnim/3), y = -boxAnim/4)
                    .clickable { mVM.deleteToken(token.uuid) }

            )
        }
        AnimatedVisibility(
            visible = token.isSelected,
            enter = slideIn(initialOffset = {IntOffset(0, it.height * 2)},
            animationSpec = tween(500, 100)),
            exit = slideOut(targetOffset = {IntOffset(0, it.height * 2)},
            animationSpec = tween(400, 100))
        ) {
            Icon(
                imageVector = Icons.Filled.ContentCopy,
                tint = Color.White,
                contentDescription = "duplicate",
                modifier = Modifier
                    .offset(x = 0.toDp, y = -boxAnim/3)
                    .clickable { mVM.duplicateToken(token) }
            )
        }
        AnimatedVisibility(
            visible = token.isSelected,
            enter = slideIn(initialOffset = {IntOffset(-it.width * 2, it.height * 2)},
            animationSpec = tween(500, 200)),
            exit = slideOut(targetOffset = {IntOffset(-it.width * 2, it.height * 2)},
            animationSpec = tween(500, 200))
        ) {
            Icon(
                imageVector = Icons.Filled.AvTimer,
                tint = Color.White,
                contentDescription = "initiative",
                modifier = Modifier
                    .offset(x = boxAnim/3, y = -boxAnim/4)
                    .clickable { mVM.setTokenInitiative(10, token.uuid) }
            )
        }

        SingleToken( mVM, token)
        Text(
            token.name,
            color = Color.White,
            fontSize = 15.sp,
            fontFamily = celestia,
            modifier = Modifier
                .width((300 * 0.8F).toDp)
                .height(90.toDp)
                .offset(x = 15.toDp, y = boxAnim* 0.1F)
                .padding(top = 25.toDp, bottom = 3.toDp, start = 25.toDp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun SingleToken( mVM: MainViewModel, token: Token) {

    Icon(
        painterResource(token.drawableRes),
        token.name,
        tint = Color.Unspecified,
        modifier = Modifier
            .size((mVM.globalScale * token.size * 100).toDp)
            .border(
                width = animateDpAsState(
                    targetValue = if (token.isSelected) 14.toDp else 0.toDp,
                    animationSpec = tween(800),
                    label = "border animation"
                ).value,
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, drag, zoom, _ ->
                    if (zoom == 1F) mVM.updateTokenPosition(drag, token.uuid)
                    if (!mVM.backgroundMap.isSelected) mVM.updateTokenSize(zoom, mVM.getSelectedTokenUuid())
                }
            }
            .clickable(
                remember { MutableInteractionSource() },
                indication = null
            ) { mVM.makeTokenSelected(token) }

    )
}











