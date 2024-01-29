package com.example.dungeonmap.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dungeonmap.MainViewModel
import com.example.dungeonmap.utilities.toDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPickerDrawer( mVM: MainViewModel) {
    val mapState by mVM.backgroundMap.collectAsState()

    AnimatedVisibility(
        visible = mapState.isMapPickerVisible,
        enter = slideInVertically (
            initialOffsetY = { it },
            animationSpec = tween(500)
        ) + fadeIn(animationSpec = tween(500)),
        exit = slideOutVertically (
            targetOffsetY = { it },
            animationSpec = tween(500)
        ) + fadeOut (animationSpec = tween(500))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { mVM.setMapPickerState(false) },
            contentAlignment = Alignment.BottomCenter

        )   {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxSize(0.9F)
                    .offset(y = 40.dp),
                drawerShape = RoundedCornerShape(10.dp),

                ) {
                Surface (
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            3.dp,
                            MaterialTheme.colorScheme.onBackground,
                            RoundedCornerShape(10.dp)
                        ),
                    shadowElevation = 15.dp,
                    color = MaterialTheme.colorScheme.secondary

                ){
                    LazyVerticalGrid(
                        contentPadding = PaddingValues(start = 5.dp, end = 5.dp, top = 15.dp, bottom = 80.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                3.dp,
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(10.dp)
                            ),
                        columns = GridCells.Adaptive(minSize = 300.toDp),
                        content = {
                            mVM.stockMapsList.forEach {
                                item {
                                    Icon(
                                        painterResource(it.first),
                                        it.second,
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(18.dp))
                                            .padding(3.dp)
                                            .clickable {
                                                mVM.updateMapImageResource(it.first)
                                                mVM.setMapPickerState(false)
                                            }



                                    )
                                }
                            }
                        }
                    )

                }
            }
        }
    }
}