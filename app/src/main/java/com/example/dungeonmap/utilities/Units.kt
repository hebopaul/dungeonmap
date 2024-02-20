package com.example.dungeonmap.utilities


import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


val Dp.toPx      : Int   @Composable get() { return with(LocalDensity.current) { roundToPx() } }
val Dp.toPxFloat : Float @Composable get() { return with(LocalDensity.current) { toPx() } }

val Int.toDp   : Dp @Composable get() = with(LocalDensity.current) { toDp() }
val Float.toDp : Dp @Composable get() = with(LocalDensity.current) { toDp() }

val SCREEN_WIDTH:  Dp @Composable get()  = LocalConfiguration.current.screenWidthDp.toDp
val SCREEN_HEIGHT: Dp @Composable get()  = LocalConfiguration.current.screenHeightDp.toDp

