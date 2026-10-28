package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "loading")

    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val alpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    val description = stringResource(R.string.loading_indicator)

    Canvas(
        modifier = modifier
            .size(60.dp)
            .semantics { contentDescription = description }
    ) {
        val brush = Brush.sweepGradient(
            0f to TolkienGold.copy(alpha = alpha * 0.4f),
            0.5f to TolkienGold.copy(alpha = alpha),
            1f to TolkienGold.copy(alpha = alpha * 0.4f),
            center = center
        )

        drawArc(
            brush = brush,
            startAngle = rotation - 30f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(
                width = 4.5.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}