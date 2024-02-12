package com.example.dungeonmap.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.ui.theme.celestia
import com.example.dungeonmap.utilities.toDp
import kotlinx.coroutines.launch


@Composable
fun InitiativeTracker(mVM: MainViewModel) {

    val tokenList = mVM._activeTokenList
    val listSize = 100
    Column{
        val listState = rememberLazyListState()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.toDp)
        ) {
            Surface( modifier = Modifier
                .fillMaxSize()
                .alpha(0.5F), color = Color.Black ) {}
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .fadingEdges(400F),
                state = listState,
                userScrollEnabled = false
            ) {
                items((listSize)) { index ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val i = index % tokenList.size
                        Icon(
                            painterResource(tokenList[i].drawableRes),
                            "Initiative Tracker Token",
                            Modifier
                                .fillMaxHeight(
                                    animateFloatAsState(
                                        if (index == mVM.currentToken) 0.8F
                                        else 0.6F
                                    ).value
                                )
                                .align(Alignment.Center)
                                .padding(horizontal = 7.toDp, vertical = 7.toDp),
                            Color.Unspecified
                        )
                        Text(
                            tokenList[index % tokenList.size].name,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontFamily = celestia,
                            modifier = Modifier
                                .width((300 * 0.8F).toDp)
                                .height(90.toDp)
                                .offset(x = 15.toDp)
                                .padding(top = 25.toDp, bottom = 3.toDp, start = 25.toDp)
                                .align(Alignment.BottomCenter)
                        )
                        Surface(
                            modifier = Modifier
                                .size(80.toDp)
                                .align(Alignment.TopEnd)
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.error
                        ) {
                            Box (
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    tokenList[i].currentInitiative.toString(),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }
        }
        val coroutineScope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.toDp)
                .alpha(0.5F)
                .clickable {

                },
            contentAlignment = Alignment.BottomCenter
        ){
            Row(
                Modifier.fillMaxWidth().height(150.toDp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    onClick = {coroutineScope.launch {
                        mVM.previousTokenClicked(!listState.canScrollBackward)
                        listState.animateScrollToItem(mVM.currentToken - 2)
                    }}
                ) {
                    Text (
                        "PREVIOUS",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    onClick = {coroutineScope.launch {
                            mVM.nextTokenClicked(!listState.canScrollForward)
                            listState.animateScrollToItem(mVM.currentToken - 2)
                    }}
                ) {
                    Text (
                        "NEXT",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.Red
                    ),
                    onClick = {mVM.endBattle()}
                ) {
                    Text (
                        "END",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }

        }
    }
}

