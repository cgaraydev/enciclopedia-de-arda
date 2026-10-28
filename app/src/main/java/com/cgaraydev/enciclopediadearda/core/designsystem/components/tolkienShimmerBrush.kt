package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold

@Composable
fun tolkienShimmerBrush(
    colors: List<Color> = listOf(TolkienGold, Color.White, TolkienGold),
    duration: Int = 3000
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val animatedProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "progress"
    )

    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = Offset(x = 600f * animatedProgress, y = 600f * animatedProgress)
    )
}