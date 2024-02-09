package com.example.dungeonmap.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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


@Composable
fun InitiativeTracker(mVM: MainViewModel) {

    val tokenList = mVM._activeTokenList
    Column{
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.toDp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5F),
                color = Color.Black
            ) {}
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .fadingEdges(400F)
            ) {
                items((100)) { index ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val i = index % tokenList.size
                        Icon(
                            painterResource(tokenList[i].drawableRes),
                            "Initiative Tracker Token",
                            Modifier
                                .fillMaxHeight(0.8F)
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
                                .offset(x = 30.toDp)
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.toDp)
                .alpha(0.5F)
                .clickable { },
            contentAlignment = Alignment.BottomCenter
        ){
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {}
            Text(
                "N E X T",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxHeight()

            )

        }
    }
}

