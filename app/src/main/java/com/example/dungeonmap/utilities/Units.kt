package com.example.dungeonmap.utilities


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val Dp.toPx      : Int   @Composable get() { return with(LocalDensity.current) { roundToPx() } }
val Dp.toPxFloat : Float @Composable get() { return with(LocalDensity.current) { toPx() } }

val Int.toDp   : Dp @Composable get() = with(LocalDensity.current) { toDp() }
val Float.toDp : Dp @Composable get() = with(LocalDensity.current) { toDp() }

val SCREEN_WIDTH_DP:  Dp @Composable get()  = LocalConfiguration.current.screenWidthDp.dp
val SCREEN_HEIGHT_DP: Dp @Composable get()  = LocalConfiguration.current.screenHeightDp.dp

