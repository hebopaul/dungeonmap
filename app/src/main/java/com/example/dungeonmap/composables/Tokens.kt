package com.example.dungeonmap.composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.utilities.toDp

@Composable
fun Tokens(mVM: MainViewModel) {
    val tokenList = mVM.activeTokenList.collectAsState()

    tokenList.value.forEach { token ->
        Icon(
            painterResource(token.imageResource),
            token.name ?: "",
            tint = Color.Unspecified,
            modifier = Modifier
                .offset(
                    x = token.position.x.toDp,
                    y = token.position.y.toDp
                )
                .scale((token.scale * token.tokenSize))
                .pointerInput(Unit) {

                    detectDragGestures { _, dragAmount ->
                        mVM.updateTokenOffset(dragAmount, token.tokenId)
                    }
                }
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress { _, dragAmount ->
                        mVM.updateTokenSize(dragAmount.y, token.tokenId)
                    }
                }
                .animateContentSize()
        )
    }
}