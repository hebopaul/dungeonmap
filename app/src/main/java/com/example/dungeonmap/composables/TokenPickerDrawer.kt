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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
fun TokenPickerDrawer( mVM: MainViewModel) {

    val mapState by mVM.backgroundMap.collectAsState()

    AnimatedVisibility(
        visible = mapState.isTokenPickerVisible,
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
                .clickable { mVM.setTokenPickerState(false) },
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
                    Column {
                        Spacer(modifier = Modifier.height(30.toDp))
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            columns = GridCells.Adaptive(minSize = 300.toDp)
                        ) {
                            mVM.stockTokensList.forEach {
                                item {
                                    Icon(
                                        painterResource(it.first),
                                        it.second,
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                mVM.createToken(it.first)
                                                mVM.setTokenPickerState(false)
                                            }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(120.toDp))
                    }
                }
            }
        }
    }
}