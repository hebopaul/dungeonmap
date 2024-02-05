package com.example.dungeonmap.utilities

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import kotlin.time.Duration

fun <T> tween(
    duration: Duration,
    delay: Duration = Duration.ZERO,
    easing: Easing = FastOutSlowInEasing
) = androidx.compose.animation.core.tween<T>(
    durationMillis = duration.inWholeMilliseconds.toInt(),
    delayMillis = delay.inWholeMilliseconds.toInt(),
    easing = easing
)
